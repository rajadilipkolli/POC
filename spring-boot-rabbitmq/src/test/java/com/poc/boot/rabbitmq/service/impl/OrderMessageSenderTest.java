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

package com.poc.boot.rabbitmq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.boot.rabbitmq.config.RabbitConfig;
import com.poc.boot.rabbitmq.util.MockObjectCreator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderMessageSenderTest {

	@Mock
	RabbitTemplate rabbitTemplate;

	@Mock
	ObjectMapper objectMapper;

	@InjectMocks
	OrderMessageSenderImpl orderMessageSender;

	@Test
	public void testSendOrder() throws JsonProcessingException {
		String convertedString = "{\"orderNumber\":\"1\",\"productId\":\"P1\"," + "\"amount\":10.0}";
		// given
		given(this.objectMapper.writeValueAsString(MockObjectCreator.getOrder())).willReturn(convertedString);
		willDoNothing().given(this.rabbitTemplate).convertAndSend(RabbitConfig.QUEUE_ORDERS,
				this.orderMessageSender.getRabbitMQMessage(convertedString));
		// when
		this.orderMessageSender.sendOrder(MockObjectCreator.getOrder());
		verify(this.rabbitTemplate, times(1)).convertAndSend(RabbitConfig.QUEUE_ORDERS,
				this.orderMessageSender.getRabbitMQMessage(convertedString));
		verify(this.objectMapper, times(1)).writeValueAsString(MockObjectCreator.getOrder());
	}

	@Test
	public void testSendOrderException() throws Exception {
		given(this.objectMapper.writeValueAsString(MockObjectCreator.getOrder()))
				.willThrow(new JsonProcessingException("Exception") {
					private static final long serialVersionUID = 1L;
				});
		// when
		assertThatThrownBy(() -> this.orderMessageSender.sendOrder(MockObjectCreator.getOrder()))
				.hasMessage("Exception");
		verify(this.objectMapper, times(1)).writeValueAsString(MockObjectCreator.getOrder());
	}

}
