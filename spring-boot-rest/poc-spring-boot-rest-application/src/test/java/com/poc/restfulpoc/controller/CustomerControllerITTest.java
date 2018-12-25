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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.data.DataBuilder;
import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.repository.CustomerRepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CustomerController Tests.
 *
 * @author Raja Kolli
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
class CustomerControllerITTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private DataBuilder dataBuilder;

	@Autowired
	private CacheManager cacheManager;

	private static final String BASEURL = "/rest/customers/";

	@BeforeAll
	void setUp() throws Exception {
		this.customerRepository.deleteAll();
		this.dataBuilder.run();
	}

	@Test
	void test01_GetCustomerById() throws Exception {
		final Long customerId = getCustomerIdByFirstName("Raja");
		final ResponseEntity<Customer> response = userRestTemplate()
				.getForEntity(String.format("%s%s", BASEURL, customerId), Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType().toString())
				.isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);

		final Customer customer = response.getBody();

		assertThat(customer.getFirstName()).isEqualTo("Raja");
		assertThat(customer.getLastName()).isEqualTo("Kolli");
		assertThat(customer.getDateOfBirth()).isEqualTo("1982-01-10T00:00:00");
		assertThat(customer.getAddress().getStreet()).isEqualTo("High Street");
		assertThat(customer.getAddress().getTown()).isEqualTo("Belfast");
		assertThat(customer.getAddress().getCounty()).isEqualTo("India");
		assertThat(customer.getAddress().getPostcode()).isEqualTo("BT893PY");
	}

	@Test
	void test02_GetCustomerByNullId() throws Exception {
		final ResponseEntity<String> response = userRestTemplate()
				.getForEntity(String.format("%s/%s", BASEURL, null), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("Test with Id that doesn't exist")
	void test03_GetCustomerByIdWhichDoesntExist() throws Exception {
		final ResponseEntity<Customer> response = userRestTemplate().getForEntity(
				String.format("%s/%s", BASEURL, Long.MAX_VALUE), Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void test04_GetAllCustomers() throws Exception {
		final ResponseEntity<String> response = userRestTemplate().getForEntity(BASEURL,
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		final List<Customer> customers = convertJsonToCustomers(response.getBody());
		assertThat(customers.size()).isGreaterThanOrEqualTo(3);
	}

	@Test
	@DisplayName("Creating Customer")
	void test05_CreateCustomer() throws Exception {
		final Customer customer = Customer.builder().firstName("Gary").lastName("Steale")
				.dateOfBirth(LocalDateTime.of(1984, Month.MARCH, 8, 0, 0)).build();
		customer.setAddress(Address.builder().street("Main Street").town("Portadown")
				.county("Armagh").postcode("BT359JK").build());
		ResponseEntity<Customer> response = userRestTemplate().postForEntity(BASEURL,
				customer, Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getContentLength()).isEqualTo(0);
		String location = response.getHeaders().getFirst("Location");
		assertThat(location).contains(BASEURL);

		response = userRestTemplate().getForEntity(location, Customer.class);
		Customer returnedCustomer = response.getBody();
		assertThat(customer.getFirstName()).isEqualTo(returnedCustomer.getFirstName());
		assertThat(customer.getLastName()).isEqualTo(returnedCustomer.getLastName());
		assertThat(customer.getDateOfBirth())
				.isEqualTo(returnedCustomer.getDateOfBirth());
		assertThat(customer.getAddress().getStreet())
				.isEqualTo(returnedCustomer.getAddress().getStreet());
		assertThat(customer.getAddress().getTown())
				.isEqualTo(returnedCustomer.getAddress().getTown());
		assertThat(customer.getAddress().getCounty())
				.isEqualTo(returnedCustomer.getAddress().getCounty());
		assertThat(customer.getAddress().getPostcode())
				.isEqualTo(returnedCustomer.getAddress().getPostcode());

		Customer newCustomer = Customer.builder().firstName("Andy").lastName("Steale")
				.build();
		newCustomer.setAddress(Address.builder().street("Main Street").town("Portadown")
				.county("Armagh").postcode("BT359JK").build());
		response = userRestTemplate().postForEntity(BASEURL, newCustomer, Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getContentLength()).isEqualTo(0);

		location = response.getHeaders().getFirst("Location");
		assertThat(location).contains(BASEURL);

		response = userRestTemplate().getForEntity(location, Customer.class);
		returnedCustomer = response.getBody();
		assertThat(returnedCustomer.getDateOfBirth()).isNull();

		newCustomer = Customer.builder().firstName("Gary").lastName("Steale")
				.address(Address.builder().street("Main Street").town("Portadown")
						.county("Armagh").postcode("BT359JK").build())
				.build();
		response = userRestTemplate().postForEntity(BASEURL, newCustomer, Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	@DisplayName("Tests InValid Customer")
	void test06_InValidCustomer() {
		Customer newCustomer = Customer.builder().firstName(" ").lastName("Steale")
				.dateOfBirth(LocalDateTime.now().plusDays(1)).build();
		ResponseEntity<Customer> response = userRestTemplate().postForEntity(BASEURL,
				newCustomer, Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		newCustomer = Customer.builder().lastName("Steale").build();
		response = userRestTemplate().postForEntity(BASEURL, newCustomer, Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void test07_UpdateCustomer() throws Exception {
		final Long customerId = getCustomerIdByFirstName("Raja");
		final ResponseEntity<Customer> getCustomerResponse = userRestTemplate()
				.getForEntity(String.format("%s/%s", BASEURL, customerId),
						Customer.class);
		assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getCustomerResponse.getHeaders().getContentType().toString())
				.isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);

		final Customer persistedCustomer = getCustomerResponse.getBody();
		assertThat(persistedCustomer.getFirstName()).isEqualTo("Raja");
		assertThat(persistedCustomer.getLastName()).isEqualTo("Kolli");
		assertThat(persistedCustomer.getDateOfBirth()).isEqualTo("1982-01-10T00:00:00");
		assertThat(persistedCustomer.getAddress().getStreet()).isEqualTo("High Street");
		assertThat(persistedCustomer.getAddress().getTown()).isEqualTo("Belfast");
		assertThat(persistedCustomer.getAddress().getCounty()).isEqualTo("India");
		assertThat(persistedCustomer.getAddress().getPostcode()).isEqualTo("BT893PY");

		persistedCustomer.setLastName("Rooney");

		/* PUT updated customer */
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<Customer> entity = new HttpEntity<Customer>(persistedCustomer,
				headers);
		final ResponseEntity<Customer> response = userRestTemplate().exchange(
				String.format("%s/%s", BASEURL, customerId), HttpMethod.PUT, entity,
				Customer.class);

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		final Customer updatedCustomer = response.getBody();
		assertThat(updatedCustomer.getFirstName()).isEqualTo("Raja");
		assertThat(updatedCustomer.getLastName()).isEqualTo("Rooney");
		assertThat(updatedCustomer.getDateOfBirth()).isEqualTo("1982-01-10T00:00:00");
		assertThat(updatedCustomer.getAddress().getStreet()).isEqualTo("High Street");
		assertThat(updatedCustomer.getAddress().getTown()).isEqualTo("Belfast");
		assertThat(updatedCustomer.getAddress().getCounty()).isEqualTo("India");
		assertThat(updatedCustomer.getAddress().getPostcode()).isEqualTo("BT893PY");
	}

	@Test
	void test08_UpdateCustomerInValid() throws Exception {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final HttpEntity<Customer> entity = new HttpEntity<>(Customer.builder().build(),
				headers);
		final ResponseEntity<Customer> getCustomerResponse = userRestTemplate().exchange(
				String.format("%s/%s", BASEURL, 999), HttpMethod.PUT, entity,
				Customer.class);

		assertThat(getCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void test09_RemoveInValidCustomer() throws Exception {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<Object> response = adminRestTemplate().exchange(
				String.format("%s/%s", BASEURL, 999), HttpMethod.DELETE,
				new HttpEntity<Customer>(Customer.builder().build(), headers),
				Object.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void test10_validateCache() {
		final Cache customersCache = this.cacheManager.getCache("customer");
		assertThat(customersCache).isNotNull();
		customersCache.clear(); // Simple test assuming the cache is empty
		final Long custId = getCustomerIdByFirstName("Raja");
		assertThat(customersCache.get(custId)).isNull();
		final ResponseEntity<Customer> response = userRestTemplate()
				.getForEntity(String.format("%s/%s", BASEURL, custId), Customer.class);
		final Customer customer = response.getBody();
		final Customer cacheCustomer = (Customer) customersCache.get(custId).get();
		assertThat(cacheCustomer.getFirstName()).isEqualTo(customer.getFirstName());
		assertThat(cacheCustomer.getLastName()).isEqualTo(customer.getLastName());
		assertThat(cacheCustomer.getDateOfBirth()).isEqualTo(customer.getDateOfBirth());
		assertThat(cacheCustomer.getAddress().getCounty())
				.isEqualTo(customer.getAddress().getCounty());
	}

	@Test
	@Disabled
	void test11_RemoveCustomer() throws Exception {
		final Long customerId = getCustomerIdByFirstName("Raja");
		final ResponseEntity<Customer> response = userRestTemplate().getForEntity(
				String.format("%s/%s", BASEURL, customerId), Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType().toString())
				.isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);

		final Customer customer = response.getBody();
		assertThat(customer.getFirstName()).isEqualTo("Raja");
		assertThat(customer.getLastName()).isEqualTo("Rooney");
		assertThat(customer.getDateOfBirth()).isEqualTo("1982-01-10T00:00:00");
		assertThat(customer.getAddress().getStreet()).isEqualTo("High Street");
		assertThat(customer.getAddress().getTown()).isEqualTo("Belfast");
		assertThat(customer.getAddress().getCounty()).isEqualTo("India");
		assertThat(customer.getAddress().getPostcode()).isEqualTo("BT893PY");

		/* delete customer */
		adminRestTemplate().delete(String.format("%s/%s", BASEURL, customerId),
				Customer.class);

		// Sleeping for 1 second so that JMS message is consumed
		try {
			TimeUnit.SECONDS.sleep(1);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		/* attempt to get customer and ensure we get a 404 */
		final ResponseEntity<Customer> secondCallResponse = userRestTemplate()
				.getForEntity(String.format("%s/%s", BASEURL, customerId),
						Customer.class);
		assertThat(secondCallResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void test12_DeleteAllCustomers() throws Exception {
		/* delete customer */
		adminRestTemplate().delete(BASEURL);

		final ResponseEntity<Customer> response = userRestTemplate().getForEntity(BASEURL,
				Customer.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	/**
	 * Convenience method for testing that gives us the customer id based on test
	 * customers name. Need this as IDs will increment as tests are rerun
	 * @param firstName the customer firstName.
	 * @return customer Id
	 */
	private Long getCustomerIdByFirstName(String firstName) {
		SQLStatementCountValidator.reset();
		long customerId = this.customerRepository.findOptionalIdByFirstName(firstName)
				.orElse(0L);
		SQLStatementCountValidator.assertInsertCount(0);
		SQLStatementCountValidator.assertUpdateCount(0);
		SQLStatementCountValidator.assertDeleteCount(0);
		SQLStatementCountValidator.assertSelectCount(1);
		return customerId;
	}

}
