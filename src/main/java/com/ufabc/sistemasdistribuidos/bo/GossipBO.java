package com.ufabc.sistemasdistribuidos.bo;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufabc.sistemasdistribuidos.client.UdpIntegrationClient;
import com.ufabc.sistemasdistribuidos.dto.global.Instancia;
import com.ufabc.sistemasdistribuidos.dto.local.Estado;
import com.ufabc.sistemasdistribuidos.dto.local.FileDTO;
import com.ufabc.sistemasdistribuidos.repository.local.EstadoRepository;
import com.ufabc.sistemasdistribuidos.repository.local.FileRepository;
import com.ufabc.sistemasdistribuidos.service.PicsumService;

@EnableScheduling
@EnableJpaRepositories(basePackages = "com.ufabc.sistemasdistribuidos.repository.local", entityManagerFactoryRef = "localEntityManager", transactionManagerRef = "localTransactionManager")
@ComponentScan("com.ufabc.sistemasdistribuidos.dto.local")
@Component
public class GossipBO {

	@Autowired
	private EstadoRepository repo;

	@Autowired
	private FileRepository repoFile;

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

		if (repo.count() < 1) {
			getNewFiles();

			Estado estado = new Estado();
			estado.setFiles(obtemMetadados(estado));
			estado.setTime(new Date());

			log.info("Salvando minhas informações: " + repo.save(estado).toString());

			repo.flush();
		}
	}

	/**
	 * obem metadados
	 */

	public List<FileDTO> obtemMetadados(Estado estado) {

		List<FileDTO> files = new ArrayList<FileDTO>();

		File f = new File("arquivos");// passar localização da pasta a ser lida
		File[] arquivos = f.listFiles();// le tudo e guarda em um array

		if (arquivos != null) {
			for (int i = 0; i < arquivos.length; i++) {
				FileDTO file = new FileDTO();
				file.setName(arquivos[i].getName());
				file.setEstado(estado);
				files.add(file);
			}
		}

		return files;
	}

	private List<FileDTO> getNewFiles() {
		List<FileDTO> files = new ArrayList<FileDTO>();

		try {
			files = picsum.loadImages();

			// para cada arquivo recuperado, salva uma cópia localmente e atualiza o nome
			for (int i = 0; i < files.size(); i++) {
				String fileName = RandomStringUtils.randomAlphabetic(10).concat(".jpg");
				FileUtils.copyURLToFile(new URL(files.get(i).getUrl()), new File("arquivos/".concat(fileName)));

				files.get(i).setName(fileName);
				log.info("Novo arquivo gerado: " + files.get(i).getName());
			}
		} catch (Exception e) {
			log.error("Erro ao carregar novas imagens", e);
		}
		return files;
	}

	/**
	 * Transmite estado atual
	 */
	@Scheduled(fixedDelay = SEGUNDO * 10)
	@Transactional("localTransactionManager")
	public void transmiteEstado() {

		try {

			Instancia d = bd.getRandomDyno();

			UnicastSendingMessageHandler unicastSendingMessageHandler;

			String host = InetAddress.getByName(d.getHost()).getHostAddress();

			unicastSendingMessageHandler = new UnicastSendingMessageHandler(host, d.getPort(), true);

			UdpIntegrationClient udp = new UdpIntegrationClient(unicastSendingMessageHandler);

			ObjectMapper obj = new ObjectMapper();
			List<Estado> estados = repo.findAll();

			// envia cada estado
			for (Estado estado : estados) {
				List<FileDTO> files = estado.getFiles();
				
				for (FileDTO file : files)
					udp.sendMessage(obj.writeValueAsString(file));
			}

		} catch (Exception e) {
			log.error("Erro ao transmitir mensagem.", e);
		}

	}

	/**
	 * Recebe um JSON com uma lista de estados a serem atualizados e os atualiza
	 * 
	 * @param mensagem
	 */
	@Transactional("localTransactionManager")
	public void atualizaEstado(String mensagem) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			FileDTO file = mapper.readValue(mensagem, FileDTO.class);

			repoFile.save(file);

			log.info("Estados internos atualizados.");
		} catch (IOException e) {
			log.error("Erro ao ler JSON de estados.", e);
		}
	}
}
