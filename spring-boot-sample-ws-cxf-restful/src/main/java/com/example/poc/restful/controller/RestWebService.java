package com.example.poc.restful.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/restapi/example")
@RestController
public class RestWebService {

	@GetMapping(value = "/{username}")
	public String getBasicHeader(@PathVariable("username") String username) {
		return "Welcome to Spring boot " + username + "!!!";
	}
}
