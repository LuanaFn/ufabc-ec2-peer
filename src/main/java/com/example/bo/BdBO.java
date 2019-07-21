package com.example.bo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.client.UdpIntegrationClient;
import com.example.dto.global.Instancia;
import com.example.repository.global.InstanciaRepository;

@Component
public class BdBO {

	private final static Logger LOGGER = LoggerFactory.getLogger(UdpIntegrationClient.class);

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
		//conta quantos obj tem no bd
		Long qt = repo.count();
		
		//calcula um index aleatório
		int i = (int)(Math.random() * qt);
		
		//captura o obj de index especificado
		Instancia d = repo.findAll().get(i);

		return d;
	}

	@PostConstruct
	@Transactional("globalTransactionManager")
	public void inicializa() {
		Instancia c = new Instancia();
		c.setHost(apphost);
		c.setPort(Integer.valueOf(udpPort));
		
		//adiciona o registro
		repo.save(c);
	}

}
