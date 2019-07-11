package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.bo.BdBO;

@Controller
@RequestMapping("/bd")
public class BdController {
	
	@Autowired
	private BdBO bo;

	@RequestMapping("/teste")
	String db(Map<String, Object> model) {
		return bo.testaBd(model);
	}
	
	@RequestMapping(path = "/init", method = RequestMethod.GET)
	String init() {
		return bo.inicializa();
	}
}
