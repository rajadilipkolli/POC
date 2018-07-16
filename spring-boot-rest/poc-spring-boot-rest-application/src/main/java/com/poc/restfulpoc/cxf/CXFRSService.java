/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.cxf;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ApacheCXF based REST Service.
 *
 * @author Raja Kolli
 *
 */
@Api("/cxf")
@ApiModel("Customer")
@Path("/cxf")
public interface CXFRSService {

	@GET
	@Path("/customers/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieves all customers from database", notes = "List of Customer details along with address is displayed", httpMethod = "GET", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Customer resource found", response = Customer.class),
			@ApiResponse(code = 404, message = "Customer resource not found") })
	Response getCustomers();

	@GET
	@Path("/customers/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a customer resource with provided Id.", notes = "Customer details along with address is displayed", httpMethod = "GET", produces = "application/json")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Customer resource found", response = Customer.class),
			@ApiResponse(code = 404, message = "Customer resource not found") })
	Response getCustomer(
			@PathParam("customerId") @ApiParam("The customerId") Long customerId)
			throws EntityNotFoundException;

	@PUT
	@Path("/customers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response updateCustomer(@PathParam("id") @ApiParam("The customerId") Long id,
			Customer customer) throws EntityNotFoundException;

	@POST
	@Path("/customers")
	@Produces(MediaType.APPLICATION_JSON)
	Response addCustomer(Customer customer, @Context UriInfo uriInfo);

}
