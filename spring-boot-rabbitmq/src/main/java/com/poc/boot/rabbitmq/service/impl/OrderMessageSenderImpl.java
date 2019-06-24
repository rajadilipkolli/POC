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
import com.poc.boot.rabbitmq.model.Order;
import com.poc.boot.rabbitmq.service.OrderMessageSender;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementation of service class.
 *
 * @author Raja Kolli
 *
 */
@Service
@RequiredArgsConstructor
public class OrderMessageSenderImpl implements OrderMessageSender {

	private final RabbitTemplate rabbitTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public void sendOrder(Order order) throws JsonProcessingException {
		// this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_ORDERS, order);

		String orderJson = this.objectMapper.writeValueAsString(order);
		this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_ORDERS, getRabbitMQMessage(orderJson));

	}

	protected Message getRabbitMQMessage(String orderJson) {
		return MessageBuilder.withBody(orderJson.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.build();
	}

}
