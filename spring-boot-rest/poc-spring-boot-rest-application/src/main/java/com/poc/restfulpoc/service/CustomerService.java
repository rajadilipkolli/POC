package com.poc.restfulpoc.service;

import java.util.List;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;

public interface CustomerService {

	void deleteCustomerById(Long customerId) throws EntityNotFoundException;

	void deleteAllCustomers();

	boolean isCustomerExist(String firstName);

	List<Customer> getCustomers();

	Customer createCustomer(Customer customer);

	Customer updateCustomer(Customer customer, Long customerId) throws EntityNotFoundException;

	Customer getCustomer(Long customerId) throws EntityNotFoundException;

}
