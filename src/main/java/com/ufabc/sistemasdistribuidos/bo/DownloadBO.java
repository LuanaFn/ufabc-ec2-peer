package com.ufabc.sistemasdistribuidos.bo;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.ufabc.sistemasdistribuidos.dto.global.Instancia;
import com.ufabc.sistemasdistribuidos.dto.local.FileDTO;
import com.ufabc.sistemasdistribuidos.repository.local.EstadoRepository;
import com.ufabc.sistemasdistribuidos.repository.local.FileRepository;
import com.ufabc.sistemasdistribuidos.service.Peer;
import com.ufabc.sistemasdistribuidos.service.UdpInboundMessageHandler;

@EnableJpaRepositories(basePackages = "com.ufabc.sistemasdistribuidos.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
@ComponentScan("com.ufabc.sistemasdistribuidos.dto.local")
@Component
public class DownloadBO {
	private final static Logger log = LoggerFactory.getLogger(UdpInboundMessageHandler.class);
	
	@Autowired
	private EstadoRepository repoEstado;

	@Autowired
	private FileRepository repoFile;
	
	@Autowired
	private Peer peer;
	
	@Autowired
	private BdBO bdbo;
	
	@Autowired
	public DownloadBO() {
		
	}
	
	public File procuraArquivo(String name, long ttl, String url) {
		//tenta encontrar o arquivo localmente
		List<FileDTO> rs = repoFile.findMeuArquivo(name);
		
		// se encontrar o arquivo
		if(rs.size() > 0) {
			log.info("Arquivo {} encontrado!", name);
			
			return new File("arquivos/"+rs.get(0).getName());
		}
		
		// tenta procurar outro repositório que conheça o arquivo
		Date instante = new Date();
		
		//se ainda não tiver dado a hora de expirar encaminha a solicitação
		if(instante.getTime() < ttl) {
			Instancia inst = bdbo.getRandomDyno();
			
			log.info("Arquivo não encontrado, redirecionando busca para {}", inst.getHost());
			
			try {
				peer.getFile(name, "http://"+inst.getHost()+":"+inst.getPort(), ttl, url);
			} catch (IOException e) {
				log.error("Erro ao redirecionar consulta ", e);
			}
		}
		
		return null;
	}
}
