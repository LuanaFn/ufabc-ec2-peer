package com.example.controller;

import java.util.Map;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/h2/*");
        return registrationBean;
    }
}
