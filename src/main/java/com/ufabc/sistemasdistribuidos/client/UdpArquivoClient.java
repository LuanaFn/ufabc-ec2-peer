package com.ufabc.sistemasdistribuidos.client;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UdpArquivoClient implements UdpClient{
	private final static Logger LOGGER = LoggerFactory.getLogger(UdpSimpleClient.class);
	
	@Value("${udp.port}")
	private Integer port;
	
	public void sendFile(byte[] arquivo) {
		
		InetSocketAddress sock = new InetSocketAddress("localhost",port);
		
		LOGGER.info("Sending file para host = " + sock.getHostName() + " e porta = "
				+ sock.getPort());

		
		DatagramPacket packet = null;
		try (DatagramSocket socket = new DatagramSocket()) {
			packet = new DatagramPacket(arquivo, arquivo.length, sock);
			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("Sending UDP file was successful");
	}

	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	
}
