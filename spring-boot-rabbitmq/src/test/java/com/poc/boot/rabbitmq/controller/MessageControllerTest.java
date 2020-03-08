/*
 * Copyright 2015-2019 the original author or authors.
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

package com.poc.boot.rabbitmq.controller;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.boot.rabbitmq.service.OrderMessageSender;
import com.poc.boot.rabbitmq.util.MockObjectCreator;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderMessageSender orderMessageSender;

	@Test
	public void testHandleMessage() throws Exception {

		willDoNothing().given(this.orderMessageSender).sendOrder(MockObjectCreator.getOrder());

		this.mockMvc
				.perform(post("/sendMsg").content(this.objectMapper.writeValueAsString(MockObjectCreator.getOrder()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(flash().attribute("message", "Order message sent successfully"))
				.andExpect(redirectedUrl("/"));
	}

	@Test
	public void testHandleMessageThrowsException() throws Exception {
		willThrow(new JsonProcessingException("Exception") {
			private static final long serialVersionUID = 1L;

		}).given(this.orderMessageSender).sendOrder(MockObjectCreator.getOrder());

		String exception = Objects.requireNonNull(this.mockMvc
				.perform(post("/sendMsg").content(this.objectMapper.writeValueAsString(MockObjectCreator.getOrder()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError()).andReturn().getResolvedException()).getMessage();

		assertThat(exception).isEqualTo("500 INTERNAL_SERVER_ERROR \"Unable To Parse Order"
				+ "(orderNumber=1, productId=P1, amount=10.0)\"; nested exception "
				+ "is com.poc.boot.rabbitmq.controller.MessageControllerTest$1: Exception");
	}

}
