package com.ufabc.sistemasdistribuidos.controller;

import java.io.FileOutputStream;
import java.io.IOException;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/download")
public class DownloadController {
	@RequestMapping(method = RequestMethod.GET)    
	public void download() throws IOException {
        
        ArquivoController get = new ArquivoController();
        
		FileOutputStream fos = new FileOutputStream("C:\\\\teste\\data2.txt");
		fos.write(get.download("lala"));
		fos.close();
	}

}
