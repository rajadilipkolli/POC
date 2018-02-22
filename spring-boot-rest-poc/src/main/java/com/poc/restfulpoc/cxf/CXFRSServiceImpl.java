/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.cxf;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CXFRSServiceImpl implements CXFRSService {

    @Autowired
    private CustomerService customerService;

    @Override
    public Response getCustomers() {
        log.info("Inside getCustomers Method");
        final List<Customer> response = customerService.getCustomers();
        if (!response.isEmpty()) {
            return Response.status(Status.OK).entity(response).build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    @Override
    public Response getCustomer(Long customerId) {
        Customer customer = null;
        try {
            customer = customerService.getCustomer(customerId);
        } catch (EntityNotFoundException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(customer).build();
    }

}
