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
public class ArquivoControlle {

	@RequestMapping(method = RequestMethod.GET)    
	public HttpEntity<byte[]> download(String path,String name) throws IOException {
		
		Arquivo file = new Arquivo();
		file.setConteudo(path);

	    
	    HttpHeaders httpHeaders = new HttpHeaders();

	    httpHeaders.add("Content-Disposition", "attachment;filename=\""+name+"\"");

	    HttpEntity<byte[]> entity = new HttpEntity<byte[]>(file.getConteudo(),httpHeaders);

	   return entity;
	    }
}
