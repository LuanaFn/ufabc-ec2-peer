package com.ufabc.sistemasdistribuidos.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.ByteStreams;
import com.ufabc.sistemasdistribuidos.bo.DownloadBO;
import com.ufabc.sistemasdistribuidos.service.Peer;
import com.ufabc.sistemasdistribuidos.service.UdpInboundMessageHandler;

@RestController
@RequestMapping("/download")
public class DownloadController {

	private final static Logger log = LoggerFactory.getLogger(UdpInboundMessageHandler.class);

	@Autowired
	DownloadBO bo;

	@Autowired
	Peer peerService;
	
	@Value("${udp.port}")
	private String udpPort;

	@Value("${app.host}")
	private String apphost;

	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public void download(@PathVariable("name") String name, HttpServletResponse response)
			throws IOException {
		log.info("Iniciando processamento do arquivo: {}", name);

		// o time to live é 10 segundos
		long ttl = new Date().getTime() + 10000;

		File file = bo.procuraArquivo(name, ttl, "http://" + apphost + ":" + udpPort);

		if (file != null) {
			InputStream stream = new FileInputStream(file);

			org.apache.commons.io.IOUtils.copy(stream, response.getOutputStream());
			response.flushBuffer();
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public void solicitaarquivo(@RequestBody String json,
			HttpServletResponse response, HttpServletRequest request) throws IOException {

		JSONObject obj = new JSONObject(json);
		String url = obj.getString("url");
		String name = obj.getString("name");
		long ttl = obj.getLong("ttl");
		
		log.info("Recebendo busca de arquivo {} que deve ser retornado a " + url, name);

		File arq = bo.procuraArquivo(name, ttl, url);

		// se encontrar o arquivo encaminha pra url que solicitou
		if (arq != null) {
			peerService.sendFile(arq, url);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/add/{name}")
	public void download(@PathVariable("name") String name, HttpServletResponse response, HttpServletRequest request) throws IOException {

		log.info("Recebendo arquivo {} solicitado", name);

		File file = new File("arquivos/"+name);
		InputStream stream = request.getInputStream();
		
		byte[] buffer = ByteStreams.toByteArray(request.getInputStream());
	    stream.read(buffer);
	    
	    OutputStream outStream = new FileOutputStream(file);
	    outStream.write(buffer);
	    
	    outStream.close();
	}
}
