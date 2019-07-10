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

public class Main {

	public static void main(String[] args) throws Exception {
	
		if(args.length < 2)
			throw new RuntimeException("O host e porta da aplicação deve ser passados como argumento");
		
		System.out.println("Porta escolhida para rodar a app: "+args[0]);
		
		Client client;
		Server server;

		server = new Server(Integer.valueOf(args[1]));
		client = new Client(args[0]); 
		
		server.start();

		client.sendEcho("hello server");
		client.sendEcho("server is working");
		
	}

}
