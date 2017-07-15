package com.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoSuchElementException(Exception e) {
		return e.getMessage();
	}

}
