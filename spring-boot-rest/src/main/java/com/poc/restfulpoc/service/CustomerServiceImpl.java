/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

/**
 * <p>CustomerServiceImpl class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final JmsTemplate jmsTemplate;

    /** {@inheritDoc} */
    @Override
    @Cacheable(value = "customer", key = "#customerId", unless = "#result == null")
    public Customer getCustomer(Long customerId) throws EntityNotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, "id",
                        customerId.toString()));
    }

    /** {@inheritDoc} */
    @Override
    public List<Customer> getCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /** {@inheritDoc} */
    @Override
    @CachePut(value = "customer", key = "#customerId")
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    /** {@inheritDoc} */
    @Override
    @CacheEvict(value = "customer", key = "#customerId")
    public void deleteCustomerById(Long customerId) {
        // Using JMS Template as the call can be asynchronous
        jmsTemplate.convertAndSend("jms.message.endpoint", customerId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCustomerExist(Customer customer) {
        final List<Customer> customerList = customerRepository
                .findByFirstName(customer.getFirstName());
        return !customerList.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    @CacheEvict(value = "customer", allEntries = true)
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }
}
