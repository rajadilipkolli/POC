/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.data;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
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
		final ZoneId defaultZoneId = ZoneId.of("UTC");

		final Customer customer1 = Customer.builder().firstName("Raja").lastName("Kolli")
				.dateOfBirth(Date.from(LocalDate.of(1982, Month.JANUARY, 10)
						.atStartOfDay(defaultZoneId).toInstant()))
				.build();
		customer1.setAddress(Address.builder().county("India").postcode("BT893PY")
				.street("High Street").town("Belfast").build());

		final Customer customer2 = Customer.builder().firstName("Paul").lastName("Jones")
				.dateOfBirth(Date.from(LocalDate.of(1973, Month.JANUARY, 03)
						.atStartOfDay(defaultZoneId).toInstant()))
				.build();
		customer2.setAddress(Address.builder().street("Main Street").town("Lurgan")
				.county("Armagh").postcode("BT283FG").build());

		final Customer customer3 = Customer.builder().firstName("Steve").lastName("Toale")
				.dateOfBirth(Date.from(LocalDate.of(1979, Month.MARCH, 8)
						.atStartOfDay(defaultZoneId).toInstant()))
				.build();
		customer3.setAddress(Address.builder().street("Main Street").town("Newry")
				.county("Down").postcode("BT359JK").build());

		this.customerRepository.saveAll(
				Stream.of(customer1, customer2, customer3).collect(Collectors.toList()));
		log.debug("Test data loaded...");
	}

}
