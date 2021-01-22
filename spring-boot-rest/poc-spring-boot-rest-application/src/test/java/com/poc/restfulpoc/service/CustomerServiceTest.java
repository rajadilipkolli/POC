/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.repository.CustomerRepository;
import com.poc.restfulpoc.repository.OrderRepository;
import com.poc.restfulpoc.service.impl.CustomerServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.jms.core.JmsTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@TestInstance(Lifecycle.PER_CLASS)
class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private JmsTemplate jmsTemplate;

	private CustomerService customerService;

	private final Customer customer = Customer.builder().firstName("firstName").lastName("lastName").build();

	@BeforeAll
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.customerService = new CustomerServiceImpl(this.customerRepository, this.orderRepository, this.jmsTemplate);
		given(this.customerRepository.findAll()).willReturn(Collections.singletonList(this.customer));
		given(this.customerRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(this.customer));
		given(this.customerRepository.save(ArgumentMatchers.any(Customer.class))).willReturn(this.customer);
		given(this.customerRepository.findByFirstName(ArgumentMatchers.anyString()))
				.willReturn(Collections.singletonList(this.customer));
		given(this.customerRepository.findByFirstName(ArgumentMatchers.eq("asdfg"))).willReturn(new ArrayList<>());

		given(this.customerRepository.findById(ArgumentMatchers.eq(0L))).willReturn(Optional.empty());

		BDDMockito.willDoNothing().given(this.jmsTemplate).convertAndSend(ArgumentMatchers.eq("jms.message.endpoint"),
				ArgumentMatchers.anyLong());
	}

	@Test
	void testGetCustomer() throws EntityNotFoundException {
		Customer customer = this.customerService.getCustomer(RandomUtils.nextLong());
		assertThat(customer).isNotNull();
		assertThat(customer.getFirstName()).isEqualTo("firstName");

		try {
			this.customerService.getCustomer(0L);
		}
		catch (EntityNotFoundException ex) {
			assertThat(ex).isExactlyInstanceOf(EntityNotFoundException.class);
		}
	}

	@Test
	void testGetCustomers() {
		List<Customer> custList = this.customerService.getCustomers();
		assertThat(custList).isNotEmpty().size().isEqualTo(1);
	}

	@Test
	void testCreateCustomer() {
		Customer res = this.customerService.createCustomer(this.customer);
		assertThat(res).isNotNull();
		assertThat(res.getFirstName()).isNotNull().isEqualTo("firstName");
	}

	@Test
	void testUpdateCustomer() throws EntityNotFoundException {
		LocalDateTime dateOfBirth = LocalDateTime.now();
		this.customer.setDateOfBirth(dateOfBirth);
		Customer res = this.customerService.updateCustomer(this.customer, RandomUtils.nextLong());
		assertThat(res).isNotNull();
		assertThat(res.getFirstName()).isNotNull().isEqualTo("firstName");
		assertThat(res.getDateOfBirth()).isNotNull().isEqualTo(dateOfBirth.toString());
	}

	@Test
	void testDeleteCustomerById() throws EntityNotFoundException {
		this.customerService.deleteCustomerById(RandomUtils.nextLong());
	}

	@Test
	void testIsCustomerExist() {
		boolean response = this.customerService.isCustomerExist(RandomStringUtils.random(5));
		assertThat(response).isEqualTo(true);
		response = this.customerService.isCustomerExist("asdfg");
		assertThat(response).isEqualTo(false);
	}

	@Test
	void testDeleteAllCustomers() {
		this.customerService.deleteAllCustomers();
	}

}
