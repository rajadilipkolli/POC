/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // From spring 4.3 way to autowire
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Get customer using id. Returns HTTP 404 if customer not found
     * 
     * @param customerId
     * @return retrieved customer
     */
    @GetMapping(value = "/rest/customers/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") @NotBlank Long customerId) {
        return customerService.getCustomer(customerId);
    }

    /**
     * Gets all customers.
     *
     * @return the customers
     */
    @GetMapping(value = "/rest/customers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    /**
     * Create a new customer and return in response with HTTP 201
     *
     * @param customer
     * @return created customer
     */
    @PostMapping(value = { "/rest/customers" })
    public Customer createCustomer(@RequestBody Customer customer,
            HttpServletResponse httpResponse, WebRequest request) {
        return customerService.createCustomer(customer, httpResponse, request);
    }

    /**
     * Update customer with given customer id.
     *
     * @param customer the customer
     */
    @PutMapping(value = { "/rest/customers/{customerId}" })
    public void updateCustomer(@RequestBody Customer customer,
            @PathVariable("customerId") Long customerId,
            HttpServletResponse httpResponse) {
        customerService.updateCustomer(customer, customerId, httpResponse);
    }

    /**
     * Deletes the customer with given customer id if it exists and returns HTTP204.
     *
     * @param customerId the customer id
     */
    @DeleteMapping(value = "/rest/customers/{customerId}")
    public void removeCustomer(@PathVariable("customerId") Long customerId,
            HttpServletResponse httpResponse) {
        customerService.deleteCustomerById(customerId, httpResponse);
    }

}
