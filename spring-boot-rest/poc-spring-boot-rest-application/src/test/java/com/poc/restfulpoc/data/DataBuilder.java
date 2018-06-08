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

package com.poc.restfulpoc.data;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.TestComponent;

/**
 * This will be used to load data in test environment
 *
 * @author rajakolli
 *
 */
@TestComponent
@RequiredArgsConstructor
@Slf4j
public class DataBuilder {

	private final CustomerRepository customerRepository;

	public void run() throws Exception {
		log.debug("Loading test data...");

		final Customer customer1 = Customer.builder().firstName("Raja").lastName("Kolli")
				.dateOfBirth(LocalDateTime.of(1982, Month.JANUARY, 10, 0, 0)).build();
		customer1.setAddress(Address.builder().county("India").postcode("BT893PY")
				.street("High Street").town("Belfast").build());

		final Customer customer2 = Customer.builder().firstName("Paul").lastName("Jones")
				.dateOfBirth(LocalDateTime.of(1973, Month.JANUARY, 03, 0, 0)).build();
		customer2.setAddress(Address.builder().street("Main Street").town("Lurgan")
				.county("Armagh").postcode("BT283FG").build());

		final Customer customer3 = Customer.builder().firstName("Steve").lastName("Toale")
				.dateOfBirth(LocalDateTime.of(1979, Month.MARCH, 8, 0, 0)).build();
		customer3.setAddress(Address.builder().street("Main Street").town("Newry")
				.county("Down").postcode("BT359JK").build());

		this.customerRepository.saveAll(
				Stream.of(customer1, customer2, customer3).collect(Collectors.toList()));
		log.debug("Test data loaded...");
	}

}
