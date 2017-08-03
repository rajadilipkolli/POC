/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.WebRequest;

import com.poc.restfulpoc.entities.Customer;

public interface CustomerService {

    Customer getCustomer(Long customerId);

    List<Customer> getCustomers();

    Customer createCustomer(Customer customer, HttpServletResponse httpResponse,
            WebRequest request);

    void updateCustomer(Customer customer, Long customerId,
            HttpServletResponse httpResponse);

    void deleteCustomerById(Long customerId, HttpServletResponse httpResponse);

}
