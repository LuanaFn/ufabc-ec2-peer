package com.ufabc.sistemasdistribuidos.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ufabc.sistemasdistribuidos.dto.local.FileDTO;

@Service
public class PicsumService {
	@Value("${picsum.url}")
	String urlpath;

	@Value("${picsum.listroute}")
	String listRoute;

	/**
	 * Carrega uma lista de imagens aleatórias
	 * 
	 * @return lista de arquivos com apenas o conteudo preenchido
	 * @throws IOException
	 */
	public List<FileDTO> loadImages() throws IOException {

		List<FileDTO> files = new ArrayList<FileDTO>();
		JSONArray resp = new JSONArray(Reader.read(urlpath.concat(listRoute)));

		for(int i=0; i< resp.length(); i++) {
			FileDTO f = new FileDTO();
			f.setUrl(resp.getJSONObject(i).getString("download_url").concat(".jpg"));
			
			files.add(f);
		}
		
		return files;
	}
}
