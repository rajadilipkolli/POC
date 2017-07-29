/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.poc.restfulpoc.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public List<Customer> findByFirstName(String firstName);

}
