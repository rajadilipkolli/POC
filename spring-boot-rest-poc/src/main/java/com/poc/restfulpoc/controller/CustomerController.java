/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>CustomerController class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@Slf4j
@RestController
@RequiredArgsConstructor // From spring 4.3 way to autowire
public class CustomerController {

    //Service which will do all data retrieval/manipulation work
    private final CustomerService customerService;
    
    /**
     * Retrieve all customers.
     *
     * @return the customers
     */
    @GetMapping(value = "/rest/customers/")
    public ResponseEntity<List<Customer>> getCustomers() {
        final List<Customer> customers = customerService.getCustomers();
        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Get customer using id. Returns HTTP 404 if customer not found
     *
     * @param customerId a {@link java.lang.Long} object.
     * @return retrieved customer
     * @throws com.poc.restfulpoc.exception.EntityNotFoundException if any.
     */
    @GetMapping(value = "/rest/customers/{customerId}", produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Customer> getCustomer(
            @PathVariable("customerId") @NotBlank Long customerId)
            throws EntityNotFoundException {
        log.info("Fetching Customer with id {}", customerId);
        final Customer user = customerService.getCustomer(customerId);
        if (user == null) {
            log.error("Customer with id {} not found", customerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Create a new customer and return in response with HTTP 201
     *
     * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
     * @return created customer
     * @param ucBuilder a {@link org.springframework.web.util.UriComponentsBuilder} object.
     */
    @PostMapping(value = { "/rest/customers/" })
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer,
            UriComponentsBuilder ucBuilder) {
        log.info("Creating Customer :{} ", customer.getFirstName());

        if (customerService.isCustomerExist(customer)) {
            log.error("A Customer with name {} already exist ", customer.getFirstName());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        customerService.createCustomer(customer);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/customers/{customerId}")
                .buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Update customer with given customer id.
     *
     * @param customer the customer
     * @param customerId a {@link java.lang.Long} object.
     * @param customerId a {@link java.lang.Long} object.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @PutMapping(value = { "/rest/customers/{customerId}" })
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer,
            @PathVariable("customerId") Long customerId) {
        log.info("Updating Customer {}", customerId);

        final Optional<Customer> currentUser = customerService.findById(customerId);

        if (!currentUser.isPresent()) {
            log.error("Customer with id {} not found ", customerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentUser.get().setFirstName(customer.getFirstName());
        currentUser.get().setLastName(customer.getLastName());
        currentUser.get().setDateOfBirth(customer.getDateOfBirth());
        currentUser.get().setAddress(customer.getAddress());

        customerService.updateCustomer(currentUser.get());
        return new ResponseEntity<>(currentUser.get(), HttpStatus.OK);
    }

    /**
     * Deletes the customer with given customer id if it exists and returns HTTP204.
     *
     * @param customerId the customer id
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @DeleteMapping(value = "/rest/customers/{customerId}")
    public ResponseEntity<Customer> removeCustomer(
            @PathVariable("customerId") Long customerId) {
        log.info("Fetching & Deleting User with id {}", customerId);

        final Optional<Customer> user = customerService.findById(customerId);
        if (!user.isPresent()) {
            log.error("Unable to delete. Customer with id {} not found", customerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /**
     * <p>deleteAllUsers.</p>
     *
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @DeleteMapping(value = "/rest/customers/")
    public ResponseEntity<Customer> deleteAllUsers() {
        log.info("Deleting All Users");
  
        customerService.deleteAllCustomers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
