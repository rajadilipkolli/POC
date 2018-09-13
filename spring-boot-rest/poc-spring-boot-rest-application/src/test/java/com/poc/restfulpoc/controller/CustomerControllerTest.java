package com.poc.restfulpoc.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.service.CustomerService;
import com.poc.restfulpoc.validator.CustomerValidator;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(secure = false)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerService customerService;

	@SpyBean
	private CustomerValidator customerValidator;

	Customer customer = Customer.builder().firstName("firstName").lastName("lastName")
			.build();

	@Test
	void testGetCustomers() throws Exception {
		given(customerService.getCustomers()).willReturn(Arrays.asList(customer));

		mockMvc.perform(MockMvcRequestBuilders.get("/rest/customers/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("[0].firstName").value("firstName"))
				.andExpect(jsonPath("[0].lastName").value("lastName"));

	}

	@Test
	void testGetCustomer() throws Exception {
		given(customerService.getCustomer(ArgumentMatchers.anyLong()))
				.willReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders.get("/rest/customers/{customerId}",
				RandomUtils.nextLong())).andExpect(status().isOk())
				.andExpect(jsonPath("firstName").value("firstName"))
				.andExpect(jsonPath("lastName").value("lastName"));
	}

	@Test
	void testCreateCustomer() throws JsonProcessingException, Exception {
		BDDMockito.willDoNothing().given(customerValidator)
				.validate(ArgumentMatchers.any(), ArgumentMatchers.any());
		given(customerService.isCustomerExist(ArgumentMatchers.anyString()))
				.willReturn(false);
		BDDMockito
				.given(customerService
						.createCustomer(ArgumentMatchers.any(Customer.class)))
				.willReturn(null);

		this.mockMvc
				.perform(post("/rest/customers/")
						.content(this.objectMapper.writeValueAsString(customer))
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());
	}

	@Test
	void testUpdateCustomer() throws Exception {
		given(customerService.getCustomer(ArgumentMatchers.anyLong()))
				.willReturn(customer);
		LocalDateTime dateOfBirth = LocalDateTime.now();
		customer.setDateOfBirth(dateOfBirth);
		given(customerService.updateCustomer(ArgumentMatchers.any(Customer.class),
				ArgumentMatchers.anyLong())).willReturn(customer);

		mockMvc.perform(MockMvcRequestBuilders
				.put("/rest/customers/{customerId}", RandomUtils.nextLong())
				.content(this.objectMapper.writeValueAsString(customer))
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(jsonPath("firstName").value("firstName"))
				.andExpect(jsonPath("lastName").value("lastName"))
				.andExpect(jsonPath("dateOfBirth").value(dateOfBirth.toString()));
	}

	@Test
	void testRemoveCustomer() throws Exception {
		BDDMockito.willDoNothing().given(customerService)
				.deleteCustomerById(ArgumentMatchers.anyLong());
		mockMvc.perform(MockMvcRequestBuilders.delete("/rest/customers/{customerId}",
				RandomUtils.nextLong())).andExpect(status().isNoContent());
	}

	@Test
	void testDeleteAllUsers() throws Exception {
		BDDMockito.willDoNothing().given(customerService).deleteAllCustomers();
		mockMvc.perform(MockMvcRequestBuilders.delete("/rest/customers/"))
				.andExpect(status().isNoContent());
	}

}
