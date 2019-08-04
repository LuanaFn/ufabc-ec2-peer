package com.ufabc.sistemasdistribuidos.dto.local;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

 
public class Arquivo implements Serializable {
    
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String nome;
    private byte[] conteudo;
    
    public String getNome() {
              return nome;
    }
    public void setNome(String nome) {
              this.nome = nome;
    }
    public byte[] getConteudo() {
              return conteudo;
    }
    //passar path completo com nome no final para leitura do arquivo 
    public void setConteudo(String path) throws IOException {
    	FileInputStream fileInStream = new FileInputStream(path);
    	int size =fileInStream.available();
        byte[] byteArray = new byte[size];
        fileInStream.read(byteArray);
        fileInStream.close();
        this.conteudo = byteArray;
    }

 }