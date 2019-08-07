package com.ufabc.sistemasdistribuidos.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ufabc.sistemasdistribuidos.client.UdpIntegrationClient;
import com.ufabc.sistemasdistribuidos.dto.global.Instancia;
import com.ufabc.sistemasdistribuidos.repository.global.InstanciaRepository;

@EnableScheduling
@EnableJpaRepositories(basePackages = "com.ufabc.sistemasdistribuidos.repository.global", entityManagerFactoryRef = "globalEntityManager", transactionManagerRef = "globalTransactionManager")
@ComponentScan("com.ufabc.sistemasdistribuidos.dto.local")
@Component
public class BdBO {

	private final static Logger log = LoggerFactory.getLogger(BdBO.class);

	private final long SEGUNDO = 1000;
	private final long MINUTO = SEGUNDO * 60;
	private final long HORA = MINUTO * 60;

	@Autowired
	private InstanciaRepository repo;

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${udp.port}")
	private String udpPort;

	@Value("${app.host}")
	private String apphost;

	@Autowired
	public BdBO() {

	}

	@Transactional("globalTransactionManager")
	public Instancia getRandomDyno() {
		// conta quantos obj tem no bd
		Long qt = repo.count();

		// calcula um index aleatório
		int i = (int) (Math.random() * qt);

		// captura o obj de index especificado
		Instancia d = repo.findAll().get(i);

		return d;
	}
	
	/**
	 * Retorna a instância salva no BD que representa o atual container
	 * ou retorna uma nova instancia se ainda não existir
	 * @return
	 */
	public Instancia getMe() {
		List<Instancia> is = repo.findByHostAndPort(apphost, Integer.valueOf(udpPort));
		
		if(is.size() > 0) {
			log.info("Eu já estava presente no BD!");
			return is.get(0);
		}
		else {
			Instancia c = new Instancia();
			c.setHost(apphost);
			c.setPort(Integer.valueOf(udpPort));
			
			log.info("Eu ainda não estava listado no BD :(");
			
			return c;
		}
	}

	@PostConstruct
	@Transactional("globalTransactionManager")
	public void inicializa() {
		repo.deleteAll();
		Instancia c = getMe();
		c.setTime(new Date());

		// adiciona o registro
		repo.save(c);

		log.info("Informações sobre a instancia foram atualizadas");
	}

	@Transactional("globalTransactionManager")
	@Scheduled(fixedDelay = MINUTO * 10)
	public void removeInstanciasInativas() {
		log.info("Iniciando limpeza do BD de instancias");

		// atualiza a instancia
		inicializa();

		// carrega a data de hoje, 10 min atras
		Date lastCheck = new Date(System.currentTimeMillis() - (10*MINUTO));

		// verifica quais instancias estão inativa a uma hora
		List<Instancia> inativos = repo.findInactives(lastCheck);

		// exclui os inativos
		repo.deleteAll(inativos);
		
		log.info(inativos.size()+" instâncias foram excluídas.");
	}
}
