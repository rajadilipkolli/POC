package com.poc.restfulpoc.service;

import java.util.List;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;

public interface CustomerService {

	Customer getCustomer(Long customerId) throws EntityNotFoundException;

	List<Customer> getCustomers();

	Customer createCustomer(Customer customer);

	Customer updateCustomer(Customer customer, Long customerId)
			throws EntityNotFoundException;

	void deleteCustomerById(Long customerId) throws EntityNotFoundException;

	boolean isCustomerExist(String firstName);

	void deleteAllCustomers();

}
