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

package com.poc.restfulpoc.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.repository.CustomerRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.springframework.jms.core.JmsTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@RunWith(JUnitPlatform.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class CustomerServiceTest {

	@Mock
	CustomerRepository customerRepository;

	@Mock
	JmsTemplate jmsTemplate;

	private CustomerService customerService;

	Customer customer = Customer.builder().firstName("firstName").lastName("lastName")
			.build();

	@BeforeAll
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.customerService = new CustomerServiceImpl(this.customerRepository,
				this.jmsTemplate);
		given(this.customerRepository.findAll()).willReturn(Arrays.asList(this.customer));
		given(this.customerRepository.findById(ArgumentMatchers.anyLong()))
				.willReturn(Optional.of(this.customer));
		given(this.customerRepository.save(ArgumentMatchers.any(Customer.class)))
				.willReturn(this.customer);
		given(this.customerRepository.findByFirstName(ArgumentMatchers.anyString()))
				.willReturn(Arrays.asList(this.customer));

		BDDMockito.willDoNothing().given(this.jmsTemplate).convertAndSend(
				ArgumentMatchers.eq("jms.message.endpoint"), ArgumentMatchers.anyLong());
	}

	@Test
	void testGetCustomer() throws EntityNotFoundException {
		Customer cust = this.customerService.getCustomer(RandomUtils.nextLong());
		assertThat(cust).isNotNull();
		assertThat(cust.getFirstName()).isEqualTo("firstName");
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
		Customer res = this.customerService.updateCustomer(this.customer,
				RandomUtils.nextLong());
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
		boolean response = this.customerService
				.isCustomerExist(RandomStringUtils.random(5));
		assertThat(response).isEqualTo(true);
	}

	@Test
	void testDeleteAllCustomers() {
		this.customerService.deleteAllCustomers();
	}

}
