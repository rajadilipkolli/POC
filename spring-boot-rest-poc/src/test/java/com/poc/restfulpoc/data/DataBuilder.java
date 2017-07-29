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

import org.springframework.boot.test.context.TestComponent;
import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        final Customer customer1 = new Customer("Raja", "Kolli",
                Date.from(LocalDate.of(1982, Month.JANUARY, 10)
                        .atStartOfDay(defaultZoneId).toInstant()),
                new Address("High Street", "Belfast", "India", "BT893PY"));

        final Customer customer2 = new Customer("Paul", "Jones",
                Date.from(LocalDate.of(1973, Month.JANUARY, 03)
                        .atStartOfDay(defaultZoneId).toInstant()),
                new Address("Main Street", "Lurgan", "Armagh", "BT283FG"));

        final Customer customer3 = new Customer("Steve", "Toale",
                Date.from(LocalDate.of(1979, Month.MARCH, 8).atStartOfDay(defaultZoneId)
                        .toInstant()),
                new Address("Main Street", "Newry", "Down", "BT359JK"));
        customerRepository.saveAll(
                Stream.of(customer1, customer2, customer3).collect(Collectors.toList()));
        log.debug("Test data loaded...");
    }

}
