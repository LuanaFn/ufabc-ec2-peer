package com.example.bo;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.example.dto.Estado;
import com.example.repository.EstadoRepository;

@EnableJpaRepositories("com.example.repository")
@Component
public class GossipBO {
	
	@Autowired
	EstadoRepository repo;
	
	@Autowired
	public GossipBO() {

	}

	/**
	 * Define um estado inicial para a máquina caso
	 * ainda não tenha nenhum
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
	public void startGossip(EstadoRepository repo) {
		String palavra = RandomStringUtils.randomAlphabetic(10);
		
		Estado estado = repo.findById(1l).get();
		estado.setMensagem(palavra);
		estado.setTime(new Date());
		
		repo.save(estado);
	}
}
