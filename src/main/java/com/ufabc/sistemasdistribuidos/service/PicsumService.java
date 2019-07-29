package com.ufabc.sistemasdistribuidos.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ufabc.sistemasdistribuidos.dto.local.File;

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
	public List<File> loadImages() throws IOException {

		List<File> files = new ArrayList<File>();
		JSONArray resp = new JSONArray(Reader.read(urlpath.concat(listRoute)));

		for(int i=0; i< resp.length(); i++) {
			File f = new File();
			f.setConteudo(getConteudoImagem(resp.getJSONObject(i)));
			
			files.add(f);
		}
		
		return files;
	}
	
	private byte[] getConteudoImagem(JSONObject json) throws JSONException, IOException {
		return Reader.read(json.getString("url")).getBytes();
	}
}
