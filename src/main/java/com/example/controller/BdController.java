package com.example.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bo.BdBO;

@Controller
public class BdController {

	@RequestMapping("/")
	String index() {
		return "index";
	}

	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		BdBO bo = new BdBO();
		return bo.testaBd(model);
	}

}
