package com.example;

import java.io.File;

public class LeArquivos {
	
	private File f;
	private File[] arquivos; 
	private String[] nomes ;

	public void le() {
		f = new File("/teste");
		arquivos = f.listFiles();
		nomes = f.list();
	}
	public void imprime() {
		
		for (int i = 0; i < nomes.length; i++) {
			System.out.println(nomes[i]);
			System.out.println(arquivos[i]);
		
		}
	}
	
	public File[] listaArquivos() {
		return arquivos;
	}
	
}
