package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;


public class Arquivo {
	
    private String path;
    
    public Arquivo(String path){
    	this.path=path;
    
    } 
 
    public void grava(String txt) throws IOException {
    	System.out.println(txt);
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path,true));
        buffWrite.append(txt);
        buffWrite.newLine();
        buffWrite.close();
    }
 
}