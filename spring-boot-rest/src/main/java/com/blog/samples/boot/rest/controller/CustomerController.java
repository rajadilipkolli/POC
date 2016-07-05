package com.blog.samples.boot.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.blog.samples.boot.rest.exception.CustomerNotFoundException;
import com.blog.samples.boot.rest.exception.InvalidCustomerRequestException;
import com.blog.samples.boot.rest.model.Customer;
import com.blog.samples.boot.rest.repository.CustomerRepository;

/**
 * Customer Controller exposes a series of RESTful endpoints
 */
@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	
	/**
	 * Get customer using id. Returns HTTP 404 if customer not found
	 * 
	 * @param customerId
	 * @return retrieved customer
	 */
	@RequestMapping(value = "/rest/customers/{customerId}", method = RequestMethod.GET)
	public Customer getCustomer(@PathVariable("customerId") Long customerId) {
		
		/* validate customer Id parameter */
		if (null==customerId) {
			throw new InvalidCustomerRequestException();
		}
		
		Customer customer = customerRepository.findOne(customerId);
		
		if(null==customer){
			throw new CustomerNotFoundException();
		}
		
		return customer;
	}

	
	/**
	 * Gets all customers.
	 *
	 * @return the customers
	 */
	@RequestMapping(value = "/rest/customers", method = RequestMethod.GET)
	public List<Customer> getCustomers() {
		
		return (List<Customer>) customerRepository.findAll();
	}

	
	/**
	 * Create a new customer and return in response with HTTP 201
	 *
	 * @param the customer
	 * @return created customer
	 */
	@RequestMapping(value = { "/rest/customers" }, method = { RequestMethod.POST })
	public Customer createCustomer(@RequestBody Customer customer, HttpServletResponse httpResponse, WebRequest request) {

		Customer createdcustomer = null;
		createdcustomer = customerRepository.save(customer);		
		httpResponse.setStatus(HttpStatus.CREATED.value());
		httpResponse.setHeader("Location", String.format("%s/rest/customers/%s", request.getContextPath(), customer.getId()));
		
		return createdcustomer;
	}

	
	/**
	 * Update customer with given customer id.
	 *
	 * @param customer the customer
	 */
	@RequestMapping(value = { "/rest/customers/{customerId}" }, method = { RequestMethod.PUT })
	public void updateCustomer(@RequestBody Customer customer, @PathVariable("customerId") Long customerId,
								   		  HttpServletResponse httpResponse) {

		if(!customerRepository.exists(customerId)){
			httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
		}
		else{
			customerRepository.save(customer);
			httpResponse.setStatus(HttpStatus.NO_CONTENT.value());	
		}
	}

	
	/**
	 * Deletes the customer with given customer id if it exists and returns HTTP204.
	 *
	 * @param customerId the customer id
	 */
	@RequestMapping(value = "/rest/customers/{customerId}", method = RequestMethod.DELETE)
	public void removeCustomer(@PathVariable("customerId") Long customerId, HttpServletResponse httpResponse) {

		if(customerRepository.exists(customerId)){
			customerRepository.delete(customerId);	
		}
		
		httpResponse.setStatus(HttpStatus.NO_CONTENT.value());
	}

}