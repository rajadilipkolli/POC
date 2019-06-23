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
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.service.CustomerService;
import com.poc.restfulpoc.validator.CustomerValidator;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@WithMockUser(roles = "USER")
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerService customerService;

	@SpyBean
	private CustomerValidator customerValidator;

	private final Customer customer = Customer.builder().firstName("firstName").lastName("lastName").build();

	@Test
	void testGetCustomers() throws Exception {
		given(this.customerService.getCustomers()).willReturn(Collections.singletonList(this.customer));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/customers/")).andExpect(status().isOk())
				.andExpect(jsonPath("[0].firstName").value("firstName"))
				.andExpect(jsonPath("[0].lastName").value("lastName"));

	}

	@Test
	void testGetCustomer() throws Exception {
		given(this.customerService.getCustomer(ArgumentMatchers.anyLong())).willReturn(this.customer);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/customers/{customerId}", RandomUtils.nextLong()))
				.andExpect(status().isOk()).andExpect(jsonPath("firstName").value("firstName"))
				.andExpect(jsonPath("lastName").value("lastName"));
	}

	@Test
	void testCreateCustomer() throws JsonProcessingException, Exception {
		BDDMockito.willDoNothing().given(this.customerValidator).validate(ArgumentMatchers.any(),
				ArgumentMatchers.any(Errors.class));
		given(this.customerService.isCustomerExist(ArgumentMatchers.anyString())).willReturn(false);
		BDDMockito.given(this.customerService.createCustomer(ArgumentMatchers.any(Customer.class))).willReturn(null);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/rest/customers/")
				.content(this.objectMapper.writeValueAsString(this.customer))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isCreated());

		final Customer invalidCustomer = this.customer;
		invalidCustomer.setFirstName(null);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/rest/customers/")
				.content(this.objectMapper.writeValueAsString(invalidCustomer))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateSameCustomer() throws Exception {
		given(this.customerService.getCustomer(ArgumentMatchers.anyLong())).willReturn(this.customer);

		this.mockMvc.perform(MockMvcRequestBuilders.put("/rest/customers/{customerId}", this.customer.getId())
				.content(this.objectMapper.writeValueAsString(this.customer))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isNotModified());
	}

	@Test
	void testUpdateCustomer() throws Exception {
		given(this.customerService.getCustomer(ArgumentMatchers.anyLong()))
				.willReturn(Customer.builder().firstName("firstName").build());

		final Customer updateRequest = this.customer;
		final LocalDateTime dateOfBirth = LocalDateTime.now();
		updateRequest.setDateOfBirth(dateOfBirth);
		given(this.customerService.updateCustomer(ArgumentMatchers.any(Customer.class), ArgumentMatchers.anyLong()))
				.willReturn(this.customer);

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/rest/customers/{customerId}", RandomUtils.nextLong())
						.content(this.objectMapper.writeValueAsString(updateRequest))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("firstName").value("firstName"))
				.andExpect(jsonPath("lastName").value("lastName"))
				.andExpect(jsonPath("dateOfBirth").value(dateOfBirth.toString()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testRemoveCustomer() throws Exception {
		BDDMockito.willDoNothing().given(this.customerService).deleteCustomerById(ArgumentMatchers.anyLong());
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/customers/{customerId}", RandomUtils.nextLong()))
				.andExpect(status().isAccepted());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testDeleteAllUsers() throws Exception {
		BDDMockito.willDoNothing().given(this.customerService).deleteAllCustomers();
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/customers/")).andExpect(status().isNoContent());
	}

}
