package com.ufabc.sistemasdistribuidos.dto.local;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class FileDTO {
	@Id
	String name;
	
	@Transient
	String url;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String conteudo) {
		this.url = conteudo;
	}
	
	
}
