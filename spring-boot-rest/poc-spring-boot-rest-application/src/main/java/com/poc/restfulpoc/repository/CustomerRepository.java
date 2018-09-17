package com.poc.restfulpoc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.restfulpoc.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByFirstName(String firstName);

}
