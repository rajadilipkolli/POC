/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.cxf;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("/cxf")
@ApiModel("Customer")
@Path("/cxf")
public interface CXFRSService {

    @GET
    @Path(value = "/customers/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all customers from database", 
        notes = "List of Customer details along with address is displayed", 
        httpMethod = "GET", 
        produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customer resource found", response = Customer.class),
            @ApiResponse(code = 404, message = "Customer resource not found") })
    Response getCustomers();

    @GET
    @Path("/customers/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets a customer resource with provided Id.", 
        notes = "Customer details along with address is displayed", 
        httpMethod = "GET", 
        produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customer resource found", response = Customer.class),
            @ApiResponse(code = 404, message = "Customer resource not found") })
    Response getCustomer(
            @PathParam("customerId") @ApiParam(value = "The customerId") Long customerId)
            throws EntityNotFoundException;
}
