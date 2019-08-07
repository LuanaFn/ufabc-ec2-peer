package com.ufabc.sistemasdistribuidos.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ufabc.sistemasdistribuidos.dto.local.Arquivo;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {

	@RequestMapping(method = RequestMethod.GET)    
	public byte[] download(String name) throws IOException {
		//TODO colocar a função de busca quando implementada para receber apenas o nome do arquivo 
		String path = "C:\\\\teste\\"+name;
		Arquivo file = new Arquivo();
		file.setNome(name);
		file.setConteudo(path);

	    
	    HttpHeaders httpHeaders = new HttpHeaders();

	    httpHeaders.add("Content-Disposition", "attachment;filename=\""+name+"\"");

//	    HttpEntity<byte[]> entity = new HttpEntity<byte[]>(file.getConteudo(),httpHeaders);

	   return file.getConteudo();
	    }
}
