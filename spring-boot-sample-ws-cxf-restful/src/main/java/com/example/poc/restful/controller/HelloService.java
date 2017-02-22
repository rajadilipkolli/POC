package com.example.poc.restful.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sayHello")
public interface HelloService {
 
    @GET
    @Path("/{a}")
    @Produces(MediaType.TEXT_PLAIN)
    String sayHello(@PathParam("a") String a);
    
}
