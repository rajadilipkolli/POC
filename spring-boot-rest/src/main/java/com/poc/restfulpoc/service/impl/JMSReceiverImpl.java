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

package com.poc.restfulpoc.service.impl;

import com.poc.restfulpoc.repository.CustomerRepository;
import com.poc.restfulpoc.service.JMSReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * JMSReceiverImpl class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JMSReceiverImpl implements JMSReceiver {

	private final CustomerRepository customerRepository;

	/** {@inheritDoc} */
	@JmsListener(destination = "jms.message.endpoint")
	@Override
	public void receiveMessage(String customerId) {
		log.info("Received CustomerID:{} for deletion", customerId);
		this.customerRepository.deleteById(Long.valueOf(customerId));
	}

}
