/*
 * Copyright 2017 the original author or authors.
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
package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.controller.WebServicesController;
import com.example.model.Book;

/**
 * @author rajakolli
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MongoDbRedisIntegrationApplicationTests {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private WebServicesController controller;

	@Test
	public void contextLoads() {
		assertThat(mongoTemplate).isNotNull();
	}

	@Test
	public void insertData() {
		controller.deleteAll();
		Book book = new Book();
		book.setTitle("MongoDbCookBook");
		book.setText("MongoDB Data Book");
		book.setAuthor("Raja");
		Book response = controller.saveBook(book);
		assertThat(response).isNotNull();
		assertThat(response.getId()).isNotBlank();
		assertThat(response.getAuthor()).isEqualTo("Raja");
	}
}
