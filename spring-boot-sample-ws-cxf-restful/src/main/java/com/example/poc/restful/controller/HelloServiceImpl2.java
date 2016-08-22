package com.example.poc.restful.controller;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;

@Path("/sayHello2")
@Api("/sayHello2")
public class HelloServiceImpl2 implements HelloService {
 
	@Override
    public String sayHello(String a) {
        return "Hello2 " + a + ", Welcome to CXF RS Spring Boot World!!!";
    }
    
}
