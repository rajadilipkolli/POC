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

package com.poc.restfulpoc.repository;

import java.time.LocalDateTime;
import java.time.Month;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.entities.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
class CustomerRepositoryTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@BeforeAll
	public void deleteAll() {
		this.orderRepository.deleteAll();
		this.customerRepository.deleteAll();
	}

	@Test
	@DisplayName("Test that deletion of customer will delete entries in order and customer")
	void testDeleteCustomer() {
		final Address address = Address.builder().county("India").postcode("BT893PY")
				.street("High Street").town("Belfast").build();
		final Order order = new Order();
		order.setOrderNumber("ORD1");
		final Customer customer = Customer.builder().firstName("Raja").lastName("Kolli")
				.dateOfBirth(LocalDateTime.of(1982, Month.JANUARY, 10, 0, 0)).build();
		customer.setAddress(address);
		customer.addOrder(order);
		Customer persistedCustomer = this.customerRepository.save(customer);

		assertThat(this.customerRepository.count()).isEqualTo(1);
		assertThat(this.orderRepository.count()).isEqualTo(1);

		this.customerRepository.delete(persistedCustomer);
		assertThat(this.customerRepository.count()).isEqualTo(0);
		assertThat(this.orderRepository.count()).isEqualTo(0);

	}

}
