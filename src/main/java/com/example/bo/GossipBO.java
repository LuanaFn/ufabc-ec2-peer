package com.example.bo;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.dto.Estado;
import com.example.repository.EstadoRepository;

@EnableScheduling
@EnableJpaRepositories("com.example.repository")
@Component
public class GossipBO {

	@Autowired
	EstadoRepository repo;

	private final long SEGUNDO = 1000;
	private final long MINUTO = SEGUNDO * 60;
	private final long HORA = MINUTO * 60;

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
	@Scheduled(fixedDelay = SEGUNDO*3)
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
	public void transmiteEstado() {
		
	}
}
