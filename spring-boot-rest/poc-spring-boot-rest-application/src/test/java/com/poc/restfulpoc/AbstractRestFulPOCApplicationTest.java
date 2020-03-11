/*
 * Copyright 2015-2018 the original author or authors.
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

package com.poc.restfulpoc;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.poc.restfulpoc.config.DataSourceProxyBeanPostProcessor;
import com.poc.restfulpoc.data.DataBuilder;
import com.poc.restfulpoc.entities.Customer;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith({ RestDocumentationExtension.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = { Application.class, DataSourceProxyBeanPostProcessor.class })
@Import(DataBuilder.class)
public abstract class AbstractRestFulPOCApplicationTest {

	@Autowired
	private Environment environment;

	@Autowired
	private ObjectMapper mapper;

	protected TestRestTemplate restTemplate() {
		return configure(new TestRestTemplate());
	}

	protected TestRestTemplate adminRestTemplate() {
		return configure(new TestRestTemplate("admin", "admin"));
	}

	protected TestRestTemplate userRestTemplate() {
		return configure(new TestRestTemplate("username", "password"));
	}

	private TestRestTemplate configure(TestRestTemplate restTemplate) {
		restTemplate.setUriTemplateHandler(new LocalHostUriTemplateHandler(this.environment));
		return restTemplate;
	}

	protected List<Customer> convertJsonToCustomers(String json) throws Exception {
		return this.mapper.readValue(json,
				TypeFactory.defaultInstance().constructCollectionType(List.class, Customer.class));
	}

	@TestConfiguration
	static class Config {

		@Bean
		RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
					.setReadTimeout(Duration.ofSeconds(1));
		}

	}

}
