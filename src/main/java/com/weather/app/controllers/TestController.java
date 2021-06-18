package com.weather.app.controllers;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weather.app.dao.UderDaoImpl;
import com.weather.app.dao.studentModel;

@Controller
public class TestController {
	
	@Autowired
	private UderDaoImpl userDaoImpl;
	
	Collection<studentModel> data;

	@RequestMapping(value={"/"})
	public String test() {		
		 data= userDaoImpl.getAllStudent();
		 for(studentModel model:data) {
			System.out.println(model.getName());
		 }
		
		return "test";
	}
}
