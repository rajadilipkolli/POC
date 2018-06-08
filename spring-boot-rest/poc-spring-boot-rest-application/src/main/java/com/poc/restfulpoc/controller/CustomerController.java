/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.ApiError;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.service.CustomerService;
import com.poc.restfulpoc.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <p>
 * CustomerController class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
@Slf4j
@RestController
@RequiredArgsConstructor // From spring 4.3 way to autowire
@RequestMapping("/rest/customers/")
public class CustomerController {

	// Service which will do all data retrieval/manipulation work
	private final CustomerService customerService;

	private final CustomerValidator customerValidator;

	/**
	 * Retrieve all customers.
	 * @return the customers
	 */
	@GetMapping
	public ResponseEntity<List<Customer>> getCustomers() {
		final List<Customer> customers = this.customerService.getCustomers();
		if (customers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}

	/**
	 * Get customer using id. Returns HTTP 404 if customer not found.
	 * @param customerId a {@link java.lang.Long} object.
	 * @return Retrieved customer.
	 * @throws EntityNotFoundException if any.
	 */
	@GetMapping(value = "{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomer(
			@PathVariable("customerId") @NotBlank Long customerId)
			throws EntityNotFoundException {
		log.info("Fetching Customer with id {}", customerId);
		final Customer user = this.customerService.getCustomer(customerId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	/**
	 * Create a new customer and return in response with HTTP 201.
	 * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @return created customer
	 * @param ucBuilder a {@link org.springframework.web.util.UriComponentsBuilder}
	 * object.
	 * @param errors a {@link org.springframework.validation.Errors} object.
	 */
	@PostMapping
	public ResponseEntity<Object> createCustomer(@Valid @RequestBody Customer customer,
			UriComponentsBuilder ucBuilder, Errors errors) {
		this.customerValidator.validate(customer, errors);
		if (errors.hasErrors()) {
			final String errorMessage = errors.getAllErrors().stream()
					.map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
			final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					new Throwable(errorMessage));
			log.error("Detailed Error while processing request :{}", apiError.toString());
			return new ResponseEntity<>(apiError, apiError.getStatus());
		}
		log.info("Creating Customer :{} ", customer.getFirstName());
		if (this.customerService.isCustomerExist(customer.getFirstName())) {
			log.error("A Customer with name {} already exist ", customer.getFirstName());
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		this.customerService.createCustomer(customer);

		final HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/rest/customers/{customerId}")
				.buildAndExpand(customer.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * Update customer with given customer id.
	 * @param customer the customer
	 * @param customerId a {@link java.lang.Long} object.
	 * @return a {@link org.springframework.http.ResponseEntity} object.
	 * @throws EntityNotFoundException if any.
	 */
	@PutMapping("{customerId}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer,
			@PathVariable("customerId") Long customerId) throws EntityNotFoundException {
		log.info("Updating Customer {}", customerId);

		final Customer updatedCustomer = this.customerService.updateCustomer(customer,
				customerId);
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}

	/**
	 * Deletes the customer with given customer id if it exists and returns HTTP204.
	 * @param customerId the customer id
	 * @return a {@link org.springframework.http.ResponseEntity} object.
	 * @throws EntityNotFoundException if any.
	 */
	@DeleteMapping("{customerId}")
	public ResponseEntity<Customer> removeCustomer(
			@PathVariable("customerId") Long customerId) throws EntityNotFoundException {
		log.info("Fetching & Deleting User with id {}", customerId);

		this.customerService.deleteCustomerById(customerId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * <p>
	 * deleteAllUsers.
	 * </p>
	 * @return a {@link org.springframework.http.ResponseEntity} object.
	 */
	@DeleteMapping
	public ResponseEntity<Customer> deleteAllUsers() {
		log.info("Deleting All Users");

		this.customerService.deleteAllCustomers();
		return ResponseEntity.noContent().build();
	}

}
