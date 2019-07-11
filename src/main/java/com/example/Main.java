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

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		ConfigurableEnvironment env = context.getEnvironment();
		printSources(env);
		System.out.println("---- System properties -----");
		printMap(env.getSystemProperties());
		System.out.println("---- System Env properties -----");
		printMap(env.getSystemEnvironment());
		
		// System.out.println("Args len: "+args.length);
		// for(int i = 0; i < args.length; i++)
		// System.out.println("ARGS["+i+"] = "+args[i]);
		
		SpringApplication.run(Main.class, args);
	}

	private static void printSources (ConfigurableEnvironment env) {
        System.out.println("---- property sources ----");
        for (PropertySource<?> propertySource : env.getPropertySources()) {
            System.out.println("name =  " + propertySource.getName() + "\nsource = " + propertySource
                                .getSource().getClass()+"\n");
        }
    }

    private static void printMap (Map<?, ?> map) {
        map.entrySet()
           .stream()
           .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

    }
}
