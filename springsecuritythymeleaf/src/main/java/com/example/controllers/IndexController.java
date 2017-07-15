package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

	@GetMapping(value = { "/", "index" })
	public String index() {
		log.debug("Getting home page");
		return "login";
	}

	@GetMapping("/home")
	public String home() {
		log.debug("Getting home page");
		return "home";
	}
}
