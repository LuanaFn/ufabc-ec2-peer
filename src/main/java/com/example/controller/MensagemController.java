package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.UdpClient;
import com.example.client.UdpIntegrationClient;

@RestController
@RequestMapping("/messages")
public class MensagemController {
private final static Logger LOGGER = LoggerFactory.getLogger(MensagemController.class);
	
	private final UdpClient udpClient;
	
	@Autowired
	public MensagemController(UdpIntegrationClient udpClient) {
		this.udpClient = udpClient;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void sendMessage(@RequestBody String message) {
		LOGGER.info("Received HTTP POST message: {}", message);
		udpClient.sendMessage(message);
	}
}
