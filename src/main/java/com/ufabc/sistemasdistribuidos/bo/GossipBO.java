package com.ufabc.sistemasdistribuidos.bo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ufabc.sistemasdistribuidos.dto.local.Estado;
import com.ufabc.sistemasdistribuidos.dto.local.File;
import com.ufabc.sistemasdistribuidos.repository.local.EstadoRepository;
import com.ufabc.sistemasdistribuidos.service.PicsumService;

@EnableScheduling
@EnableJpaRepositories(basePackages = "com.ufabc.sistemasdistribuidos.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
@ComponentScan("com.ufabc.sistemasdistribuidos.dto.local")
@Component
public class GossipBO {

	@Autowired
	private EstadoRepository repo;

	private final long SEGUNDO = 1000;
	private final long MINUTO = SEGUNDO * 60;
	private final long HORA = MINUTO * 60;

	private final static Logger log = LoggerFactory.getLogger(GossipBO.class);

	@Autowired
	BdBO bd;

	@Autowired
	PicsumService picsum;

	@Autowired
	public GossipBO() {

	}

	/**
	 * Define um estado inicial para a máquina caso ainda não tenha nenhum
	 * 
	 * @param repo
	 */
	@PostConstruct
	@Transactional("localTransactionManager")
	public void init() {
		getNewFiles();
		if (repo.count() < 1) {
			Estado estado = new Estado();
			estado.setFiles(getNewFiles());
			estado.setTime(new Date());

			log.info("Salvando minhas informações: " + repo.save(estado).toString());

			repo.flush();
		}
	}

	private List<File> getNewFiles() {
		List<File> files = new ArrayList<File>();

		try {
			files = picsum.loadImages();

			// para cada arquivo recuperado, salva uma cópia localmente e atualiza o nome
			for (int i = 0; i < files.size(); i++) {
				String fileName = RandomStringUtils.randomAlphabetic(10).concat(".jpg");

				BufferedWriter writer = new BufferedWriter(new FileWriter("arquivos/".concat(fileName)));
				writer.write(files.get(i).getConteudo());

				writer.close();

				files.get(i).setName(fileName);
				log.info("Novo arquivo gerado: "+fileName);
			}
		} catch (IOException e) {
			log.error("Erro ao carregar novas imagens", e);
		}

		return files;
	}

//	/**
//	 * Gera uma nova palavra de 10 digitos e seta como estado
//	 */
//	@Scheduled(fixedDelay = SEGUNDO * 30)
//	@Transactional("localTransactionManager")
//	public void startGossip() {
//		String palavra = RandomStringUtils.randomAlphabetic(10);
//
//		Estado estado = repo.findById(1l).get();
//		estado.setMensagem(palavra);
//		estado.setTime(new Date());
//		
//		log.info("Iniciando novo gossip com "+palavra);
//
//		repo.save(estado);
//	}
//
//	/**
//	 * Transmite estado atual
//	 */
//	@Scheduled(fixedDelay = SEGUNDO * 3)
//	@Transactional("localTransactionManager")
//	public void transmiteEstado() {
//
//		try {
//			Instancia d = bd.getRandomDyno();
//
//			UnicastSendingMessageHandler unicastSendingMessageHandler;
//			
//			String host = InetAddress.getByName(d.getHost()).getHostAddress();
//
//			unicastSendingMessageHandler = new UnicastSendingMessageHandler(
//					host, d.getPort());
//
//			UdpIntegrationClient udp = new UdpIntegrationClient(unicastSendingMessageHandler);
//
//			udp.sendMessage(repo.findById(1l).get().getMensagem());
//			
//			
//		} catch (UnknownHostException e) {
//			log.error("Erro ao transmitir mensagem.", e);
//		}
//		
//	}
//	
//	@Transactional("localTransactionManager")
//	public void atualizaEstado(String mensagem) {
//		Estado estado = repo.findAll().get(0);
//		
//		estado.setMensagem(mensagem);
//		estado.setTime(new Date());
//		
//		repo.save(estado);
//		
//		log.info("Atualizando meu estado: "+estado.toString());
//		
//		repo.flush();
//	}
}
