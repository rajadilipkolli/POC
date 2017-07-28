package com.poc.restfulpoc.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.poc.restfulpoc.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public List<Customer> findByFirstName(String firstName);

}
