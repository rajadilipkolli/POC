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
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.entities.Address;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.entities.Order;
import com.poc.restfulpoc.entities.OrderStatus;
import com.poc.restfulpoc.repository.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CustomerControllerMVC Tests.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
public class CustomerControllerMVCTest extends AbstractRestFulPOCApplicationTest {

	private static final String CUSTOMERRESOURCEURL = "/rest/customers/";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(restDocumentation))
				.alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint())))
				.apply(springSecurity()).build();
	}

	@Test
	void findAllCustomers() throws JsonProcessingException, Exception {
		createData();
		this.mockMvc
				.perform(get(CUSTOMERRESOURCEURL).with(httpBasic("username", "password"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(document("find-all-customers",
						requestHeaders(headerWithName("Authorization")
								.description("Basic auth credentials")),
						responseFields(
								fieldWithPath("[]").description("An array of customers"))
										.andWithPrefix("[].",
												getCustomerFieldDescriptor())));
	}

	@Test
	void getCustomer() throws JsonProcessingException, Exception {
		this.mockMvc
				.perform(RestDocumentationRequestBuilders
						.get(CUSTOMERRESOURCEURL + "{customerId}",
								String.valueOf(createData()))
						.with(httpBasic("username", "password"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andDo(document("get-customer",
						requestHeaders(headerWithName("Authorization")
								.description("Basic auth credentials")),
						pathParameters(parameterWithName("customerId")
								.description("Unique identifier of the Customer")),
						responseFields(getCustomerFieldDescriptor())));
	}

	@Test
	void createCustomerFullPayLoad() throws JsonProcessingException, Exception {
		final Customer customer = Customer.builder().firstName(RandomString.make(10))
				.lastName(RandomString.make(10))
				.dateOfBirth(LocalDateTime.of(1984, Month.MARCH, 8, 0, 0)).build();
		customer.setAddress(Address.builder().street("Main Street").town("Portadown")
				.county("Armagh").postcode("BT359JK").build());
		Order order = new Order();
		order.setCreatedOn(LocalDateTime.now());
		order.setOrderNumber(RandomString.make(10));
		order.setStatus(OrderStatus.IN_PROCESS);
		customer.setOrders(Arrays.asList(order));
		this.mockMvc
				.perform(post(CUSTOMERRESOURCEURL).with(httpBasic("username", "password"))
						.content(this.objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andDo(document("create-customer-full-pay-load",
						requestHeaders(headerWithName("Authorization")
								.description("Basic auth credentials")),
						responseHeaders(headerWithName("Location")
								.description("Redirected URL of the created Entity")),
						requestFields(getCustomerFieldDescriptor())));
	}

	@Test
	void updateCustomer() throws Exception {
		MockHttpServletResponse response = this.mockMvc.perform(
				get(CUSTOMERRESOURCEURL + "{customerId}", String.valueOf(createData()))
						.with(httpBasic("username", "password"))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn().getResponse();
		Customer customer = this.objectMapper.readValue(response.getContentAsByteArray(),
				Customer.class);

		String firstName = RandomString.make(10);
		customer.setFirstName(firstName);

		this.mockMvc
				.perform(RestDocumentationRequestBuilders
						.put(CUSTOMERRESOURCEURL + "{customerId}",
								String.valueOf(customer.getId()))
						.with(httpBasic("username", "password"))
						.content(this.objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.firstName", equalTo(firstName)))
				.andDo(document("update-customer",
						requestHeaders(headerWithName("Authorization")
								.description("Basic auth credentials")),
						pathParameters(parameterWithName("customerId")
								.description("Unique identifier of the Customer")),
						responseFields(getCustomerFieldDescriptor())));
	}

	private long createData() {
		if (this.repository.count() == 0) {
			Customer customer = new Customer();
			customer.setFirstName(RandomString.make(10));
			customer.setLastName(RandomString.make(10));
			customer.setDateOfBirth(LocalDateTime.now());
			Address address = new Address();
			address.setCounty(RandomString.make(10));
			address.setPostcode(RandomString.make(10));
			address.setStreet(RandomString.make(10));
			address.setTown(RandomString.make(10));
			customer.setAddress(address);
			return this.repository.save(customer).getId();
		}
		else {
			return this.repository.findAll().get(0).getId();
		}
	}

	private FieldDescriptor[] getCustomerFieldDescriptor() {
		return new FieldDescriptor[] {

				fieldWithPath("id").description("The Unique id of the customer")
						.type(Long.class.getSimpleName()),
				fieldWithPath("firstName").description("The firstname of the customer")
						.type(String.class.getSimpleName()),
				fieldWithPath("lastName").description("The lastName of the customer")
						.type(String.class.getSimpleName()),
				fieldWithPath("dateOfBirth")
						.description("The date of birth of the customer")
						.type(String.class.getSimpleName()),
				fieldWithPath("address.id").description("Unique Id of the address")
						.type(Long.class.getSimpleName()),
				fieldWithPath("address.county").description("Country of the address")
						.type(String.class.getSimpleName()),
				fieldWithPath("address.street").description("Street of the address")
						.type(String.class.getSimpleName()),
				fieldWithPath("address.town").description("Town of the address")
						.type(String.class.getSimpleName()),
				fieldWithPath("address.postcode").description("PostalCode of the address")
						.type(Order.class.getSimpleName()),
				fieldWithPath("orders").description("Orders which the customer has made")
						.type(Order.class.getSimpleName()),
				fieldWithPath("orders.[].orderId").description("Unique Id of the Order")
						.type(Long.class.getSimpleName()),
				fieldWithPath("orders.[].orderNumber")
						.description("Order Number of the Order")
						.type(String.class.getSimpleName()),
				fieldWithPath("orders.[].status").description("Status of the Order")
						.type(OrderStatus.class.getSimpleName()),
				fieldWithPath("orders.[].createdOn")
						.description("Date on which Order is created")
						.type(String.class.getSimpleName()) };
	}

}
