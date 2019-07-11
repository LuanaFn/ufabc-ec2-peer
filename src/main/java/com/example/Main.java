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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class Main {
	
	public static void main(String[] args) throws Exception {
		
		SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder(Main.class)
	            .environment(new StandardEnvironment(){
	                @Override
	                protected void customizePropertySources(MutablePropertySources propertySources) {
	                    // do not add system or env properties to the set of property sources
	                	propertySources.remove("JDBC_DATABASE_URL");
	                	propertySources.remove("SPRING_DATASOURCE_URL");
	                	propertySources.remove("DATABASE_URL");
	                	propertySources.remove("SPRING_DATASOURCE_PASSWORD");
	                	propertySources.remove("JDBC_DATABASE_USERNAME");
	                	propertySources.remove("SPRING_DATASOURCE_USERNAME"); 
	                	
	                	//chama o mesmo método sem essa variável
	                	super.customizePropertySources(propertySources);
	                }
	            });
		
//		System.out.println("Args len: "+args.length);
//		for(int i = 0; i < args.length; i++)
//			System.out.println("ARGS["+i+"] = "+args[i]);
		//SpringApplication.run(Main.class, args);
		
		applicationBuilder.run(args);
	}

}
