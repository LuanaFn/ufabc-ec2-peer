package com.example.bo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.client.UdpIntegrationClient;
import com.example.dto.global.Instancia;
import com.example.dto.local.Estado;
import com.example.repository.local.EstadoRepository;

@EnableScheduling
@EnableJpaRepositories("com.example.repository.local")
@ComponentScan("com.example.dto.local")
@Component
public class GossipBO {

	@Autowired
	private EstadoRepository repo;

	private final long SEGUNDO = 1000;
	private final long MINUTO = SEGUNDO * 60;
	private final long HORA = MINUTO * 60;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(GossipBO.class);
	

	@Autowired
	BdBO bd;

	@Autowired
	public GossipBO() {

	}

	/**
	 * Define um estado inicial para a máquina caso ainda não tenha nenhum
	 * 
	 * @param repo
	 */
	@PostConstruct
	public void init() {

		if (repo.count() < 1) {
			Estado estado = new Estado();
			estado.setMensagem("Estado inicial");
			estado.setTime(new Date());

			System.out.println(repo.save(estado).toString());

			repo.flush();
		}
	}

	/**
	 * Gera uma nova palavra de 10 digitos e seta como estado
	 */
	@Scheduled(fixedDelay = SEGUNDO * 30)
	public void startGossip() {
		String palavra = RandomStringUtils.randomAlphabetic(10);

		Estado estado = repo.findById(1l).get();
		estado.setMensagem(palavra);
		estado.setTime(new Date());

		repo.save(estado);
	}

	/**
	 * Transmite estado atual
	 */
	@Scheduled(fixedDelay = SEGUNDO * 3)
	public void transmiteEstado() {

		try {
			Instancia d = bd.getRandomDyno();

			UnicastSendingMessageHandler unicastSendingMessageHandler;

			unicastSendingMessageHandler = new UnicastSendingMessageHandler(
					InetAddress.getByName(d.getHost()).getHostAddress(), d.getPort());

			UdpIntegrationClient udp = new UdpIntegrationClient(unicastSendingMessageHandler);

			udp.sendMessage(repo.findById(1l).get().getMensagem());
			
		} catch (UnknownHostException e) {
			LOGGER.error("Erro ao transmitir mensagem.", e);
		}
		
	}
	
	public void atualizaEstado(String mensagem) {
		Estado estado = repo.findAll().get(0);
		
		estado.setMensagem(mensagem);
		estado.setTime(new Date());
		
		repo.save(estado);
		
		repo.flush();
	}
}
