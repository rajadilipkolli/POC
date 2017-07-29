/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.CustomerNotFoundException;
import com.poc.restfulpoc.exception.InvalidCustomerRequestException;
import com.poc.restfulpoc.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor //From spring 4.3 way to autowire
public class CustomerController {

    private final CustomerRepository customerRepository;

    /**
     * Get customer using id. Returns HTTP 404 if customer not found
     * 
     * @param customerId
     * @return retrieved customer
     */
    @GetMapping(value = "/rest/customers/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") Long customerId) {

        /* validate customer Id parameter */
        if (null == customerId) {
            throw new InvalidCustomerRequestException();
        }

        final Optional<Customer> customer = customerRepository.findById(customerId);

        return customer.orElseThrow(CustomerNotFoundException::new);
    }

    /**
     * Gets all customers.
     *
     * @return the customers
     */
    @GetMapping(value = "/rest/customers")
    public List<Customer> getCustomers() {

        return (List<Customer>) customerRepository.findAll();
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

        final Customer createdcustomer = customerRepository.save(customer);
        httpResponse.setStatus(HttpStatus.CREATED.value());
        httpResponse.setHeader("Location", String.format("%s/rest/customers/%s",
                request.getContextPath(), customer.getId()));

        return createdcustomer;
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

        if (!customerRepository.existsById(customerId)) {
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            customerRepository.save(customer);
            httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    /**
     * Deletes the customer with given customer id if it exists and returns HTTP204.
     *
     * @param customerId the customer id
     */
    @DeleteMapping(value = "/rest/customers/{customerId}")
    public void removeCustomer(@PathVariable("customerId") Long customerId,
            HttpServletResponse httpResponse) {

        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
        }

        httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
    }

}
