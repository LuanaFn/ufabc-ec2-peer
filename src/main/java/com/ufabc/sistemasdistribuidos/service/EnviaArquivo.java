package com.ufabc.sistemasdistribuidos.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ufabc.sistemasdistribuidos.dto.local.Arquivo;


public class EnviaArquivo {
	
	
	private byte[] arquivoToByte(Arquivo arquivo) throws IOException {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	    ObjectOutputStream objectOutStream;
	    objectOutStream = new ObjectOutputStream(byteArray);
	    objectOutStream.writeObject(arquivo);
	    return byteArray.toByteArray();
	} 
	
	public void enviar( Arquivo arquivo){
		   
		    try {

		    	Socket socket = new Socket("localhost", 6000);
		    	
		 
		        BufferedOutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
		 
		        byte[] bytea = arquivoToByte(arquivo);
		        buffer.write(bytea);
		        buffer.flush();
		        buffer.close();
		        socket.close();
		    } catch (UnknownHostException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


} 	