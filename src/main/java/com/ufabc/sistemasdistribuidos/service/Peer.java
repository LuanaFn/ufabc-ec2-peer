package com.ufabc.sistemasdistribuidos.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class Peer {

	private final static Logger log = LoggerFactory.getLogger(UdpInboundMessageHandler.class);

	public void getFile(String name, String url, long ttl, String urlResposta) throws IOException {

		log.info("Chamando serviço de busca de arquivos do host " + url + " com resposta para " + urlResposta
				+ " com o arquivo {}", name);

		URL u = new URL(url + "/download");
		HttpURLConnection con = (HttpURLConnection) u.openConnection(); // abre conexao
		con.setRequestMethod("POST"); // fala que quer um post
		con.setRequestProperty("Content-Type", "application/json; utf-8"); // fala o que vai mandar
		con.setDoOutput(true); // fala que voce vai enviar algo

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode on = mapper.createObjectNode();
		on.put("name", name);
		on.put("url", urlResposta);
		on.put("ttl", ttl);

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = on.toString().getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		con.connect(); // envia para o servidor

		Scanner scanner = new Scanner(con.getInputStream());
		String jsonDeResposta = "";

		if (scanner.hasNext())
			jsonDeResposta = scanner.next(); // pega resposta

		log.info("Enviado com sucesso. Resposta: {}", jsonDeResposta);
	}

	public void sendFile(File file, String url) throws IOException {

		URL u = new URL(url + "/download/add/" + file.getName());

		log.info("Chamando serviço de envio de arquivo na máquina do cliente {}", u.toString());

		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setDoOutput(true);

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = Files.readAllBytes(file.toPath());
			os.write(input, 0, input.length);
		}

		con.connect(); // envia para o servidor

		Scanner scanner = new Scanner(con.getInputStream());
		String jsonDeResposta = "";

		if (scanner.hasNext())
			jsonDeResposta = scanner.next(); // pega resposta

		log.info("Enviado com sucesso. Resposta: {}", jsonDeResposta);
	}
}
