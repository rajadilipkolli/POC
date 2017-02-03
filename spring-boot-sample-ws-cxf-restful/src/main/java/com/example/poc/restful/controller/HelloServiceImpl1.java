package com.example.poc.restful.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("/sayHello")
public class HelloServiceImpl1 implements HelloService
{

    @Override
    @ApiOperation(httpMethod = "GET", value = "Ping User", notes = "This acts as prototype for rest API.", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "UserName not found") })
    public String sayHello(@ApiParam(value = "userName", required = true) String userName)
    {
        return "Hello " + userName + ", Welcome to CXF RS Spring Boot World!!!";
    }

}
