/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

	public static void main(String[] args) throws Exception {
	
		if(args.length < 2)
			throw new RuntimeException("O host e porta da aplicação deve ser passados como argumento");
		
		String host = args[0];
		int porta = Integer.valueOf(args[1]);
		System.out.println("O app vai começar a rodar com o host "+host+" e a porta "+porta);
		
		Client client;
		Server server;

		server = new Server(porta);
		client = new Client(host); 
		
		server.start();

		client.sendEcho("hello server");
		client.sendEcho("server is working");

		 
		LeArquivos le = new LeArquivos();
		Arquivo g = new Arquivo("/teste/data.txt");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String data = sdf.format(cal.getTime());
		
		le.le();
		File[] arquivos = le.listaArquivos();
		for (int i = 0; i < arquivos.length; i++) {
			System.out.println(arquivos[i]);  			 
			g.grava("peer:nome_do_peer;metadados_do_arquivo:"+arquivos[i]+"hora_da_gravacao:"+data+"\n");
			
		}
	}

}
