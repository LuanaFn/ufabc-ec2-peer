package com.ufabc.sistemasdistribuidos.dto.local;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Estado {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// @JsonBackReference
	@JsonIgnore
	@OneToMany(targetEntity = FileDTO.class, mappedBy = "estado", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<FileDTO> files;

	private String host;

	private int port;

	private Date time;

	public List<FileDTO> getFiles() {
		return files;
	}

	public void setFiles(List<FileDTO> files) {
		this.files = files;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("Estado[id=%d, time=%s]", id, time);
	}
}
