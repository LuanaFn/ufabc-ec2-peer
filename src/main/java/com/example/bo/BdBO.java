package com.example.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.client.UdpIntegrationClient;
import com.example.dto.global.Instancia;
import com.example.repository.global.InstanciaRepository;

@EnableScheduling
@Component
public class BdBO {

	private final static Logger log = LoggerFactory.getLogger(UdpIntegrationClient.class);

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

	@PostConstruct
	@Transactional("globalTransactionManager")
	public void inicializa() {

		List<Instancia> me = repo.findByHost(apphost);

		Instancia c;

		if (me.size() > 0) {
			c = me.get(0);
		} else {
			c = new Instancia();
			c.setHost(apphost);
			c.setPort(Integer.valueOf(udpPort));
			c.setTime(new Date());
		}

		// adiciona o registro
		repo.save(c);

		log.debug("Informações sobre a instancia foram salvas");
	}

	@Transactional("globalTransactionManager")
	@Scheduled(fixedDelay = SEGUNDO * 10)
	public void removeInstanciasInativas() {
		log.debug("Iniciando limpeza do BD de instancias");

		// atualiza a instancia
		inicializa();

		// carrega a data de hoje, uma hora artas
		Date lastCheck = new Date(System.currentTimeMillis() - HORA);

		// verifica quais instancias estão inativa a uma hora
		List<Instancia> inativos = repo.findInactives(lastCheck);

		// exclui os inativos
		repo.deleteAll(inativos);
	}
}
