/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

import java.util.List;
import java.util.Optional;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;

public interface CustomerService {

    Customer getCustomer(Long customerId) throws EntityNotFoundException;

    List<Customer> getCustomers();

    Customer createCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomerById(Long customerId);

    boolean isCustomerExist(Customer customer);

    Optional<Customer> findById(Long customerId);

    void deleteAllCustomers();

}
