/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.CustomerNotFoundException;
import com.poc.restfulpoc.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final JmsTemplate jmsTemplate;

    @Override
    public Customer getCustomer(Long customerId) {
        final Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public List<Customer> getCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer, HttpServletResponse httpResponse,
            WebRequest request) {
        final Customer createdcustomer = customerRepository.save(customer);
        httpResponse.setStatus(HttpStatus.CREATED.value());
        httpResponse.setHeader("Location", String.format("%s/rest/customers/%s",
                request.getContextPath(), customer.getId()));

        return createdcustomer;
    }

    @Override
    public void updateCustomer(Customer customer, Long customerId,
            HttpServletResponse httpResponse) {

        if (!customerRepository.existsById(customerId)) {
            httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            // Using JMS Template as the call can be asynchronous
            jmsTemplate.convertAndSend("jms.message.endpoint", customer);
            // Adding delay to demostrate in JUNIT test cases
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
        }

    }

    @Override
    public void deleteCustomerById(Long customerId, HttpServletResponse httpResponse) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
        }
        httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
    }
}
