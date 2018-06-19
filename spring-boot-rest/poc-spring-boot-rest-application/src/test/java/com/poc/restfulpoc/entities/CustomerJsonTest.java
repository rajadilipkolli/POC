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

package com.poc.restfulpoc.entities;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
@TestInstance(Lifecycle.PER_CLASS)
class CustomerJsonTest {

	private static final String CUSTOMER_JSON = "{\"firstName\":\"Raja\",\"lastName\":\"Kolli\",\"dateOfBirth\":\"1982-01-10T00:00\",\"address\":{\"street\":\"High Street\",\"town\":\"Belfast\",\"county\":\"India\",\"postcode\":\"BT893PY\"}}";

	private Customer customerObj = null;

	@Autowired
	private JacksonTester<Customer> json;

	@BeforeAll
	public void init() {
		final Address address = Address.builder().county("India").postcode("BT893PY")
				.street("High Street").town("Belfast").build();
		final Order order = new Order();
		order.setOrderNumber("ORD1");
		this.customerObj = Customer.builder().firstName("Raja").lastName("Kolli")
				.dateOfBirth(LocalDateTime.of(1982, Month.JANUARY, 10, 0, 0, 0)).build();
		this.customerObj.setAddress(address);
		this.customerObj.addOrder(order);
	}

	@Test
	public void testSerialize() throws Exception {
		// Using JSON path based assertions
		assertThat(this.json.write(this.customerObj))
				.hasJsonPathStringValue("@.firstName");
		assertThat(this.json.write(this.customerObj))
				.extractingJsonPathStringValue("@.firstName").isEqualTo("Raja");
		assertThat(this.json.write(this.customerObj)).isEqualToJson(CUSTOMER_JSON);
	}

	@Test
	public void testDeserialize() throws Exception {
		assertThat(this.json.parseObject(CUSTOMER_JSON).getFirstName()).isEqualTo("Raja");
		// assertThat(this.json.parse(CUSTOMER_JSON)).isEqualTo(customerObj);
	}

}
