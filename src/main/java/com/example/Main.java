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
import java.sql.ResultSet;

public class Main {

	public static void main(String[] args) throws Exception {
		// SpringApplication.run(Main.class, args);
		System.out.println("teste");
		
//		Client client;
//		Server server;
//
//		server = new Server();
//		client = new Client(); 
//		
//		server.start();
//
//		client.sendEcho("hello server");
//		client.sendEcho("server is working");
		
		Sql t = new Sql();
		LeArquivos le = new LeArquivos();
		
		le.le();
		t.executaInsert("INSERT INTO lista (peer,time) VALUES ('teste','2019-07-09 15:00:00')");
		 ResultSet rs = t.executaSelect("SELECT * FROM `lista` WHERE peer = \"teste\" ORDER BY time DESC");
		 rs.next();
		 int id = rs.getInt("id");
		 File[] arquivos = le.listaArquivos();
		 for (int i = 0; i < arquivos.length; i++) {
			 System.out.println("INSERT INTO `item`( `data`, `lista_id`) VALUES ("+arquivos[i].toString()+","+id+")");
			t.executaInsert( "INSERT INTO `item`( `data`, `lista_id`) VALUES ('"+arquivos[i].toString()+"',"+id+")");
			}
		 
		
	}

}
