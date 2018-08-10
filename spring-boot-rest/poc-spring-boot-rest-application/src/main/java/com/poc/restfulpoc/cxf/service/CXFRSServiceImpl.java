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

package com.poc.restfulpoc.cxf.service;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.feature.Features;

import org.springframework.stereotype.Service;

/**
 * Implementation of ApacheCXF Rest Service.
 *
 * @author Raja Kolli
 *
 */
@Slf4j
@Service
@Features(features = "org.apache.cxf.feature.LoggingFeature")
public class CXFRSServiceImpl implements CXFRSService {

	private final CustomerService customerService;

	public CXFRSServiceImpl(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@Override
	public Response getCustomers() {
		log.info("Inside getCustomers Method");
		final List<Customer> response = this.customerService.getCustomers();
		if (!response.isEmpty()) {
			return Response.status(Status.OK).entity(response).build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@Override
	public Response getCustomer(Long customerId) {
		Customer customer = null;
		try {
			customer = this.customerService.getCustomer(customerId);
		}
		catch (EntityNotFoundException ex) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.OK).entity(customer).build();
	}

	@Override
	public Response addCustomer(Customer customer, UriInfo uriInfo) {
		if (this.customerService.isCustomerExist(customer.getFirstName())) {
			log.error("A Customer with name {} already exist ", customer.getFirstName());
			return Response.status(Status.CONFLICT).build();
		}
		this.customerService.createCustomer(customer);

		URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(customer.getId()))
				.build();
		return Response.created(uri).entity(customer).build();
	}

	@Override
	public Response updateCustomer(Long customerId, Customer customer)
			throws EntityNotFoundException {
		log.info("Updating Customer {}", customerId);
		final Customer currentUser = this.customerService.getCustomer(customerId);
		if (currentUser.equals(customer)) {
			return Response.notModified().build();
		}
		currentUser.setFirstName(customer.getFirstName());
		currentUser.setLastName(customer.getLastName());
		currentUser.setDateOfBirth(customer.getDateOfBirth());
		currentUser.setAddress(customer.getAddress());
		this.customerService.updateCustomer(customer, customerId);
		return Response.status(Status.OK).entity(currentUser).build();
	}

}
