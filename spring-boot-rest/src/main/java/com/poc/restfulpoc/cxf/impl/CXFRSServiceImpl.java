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

package com.poc.restfulpoc.cxf.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.poc.restfulpoc.cxf.CXFRSService;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of ApacheCXF Rest Service.
 *
 * @author Raja Kolli
 *
 */
@Slf4j
@Service
public class CXFRSServiceImpl implements CXFRSService {

	@Autowired
	private CustomerService customerService;

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

}
