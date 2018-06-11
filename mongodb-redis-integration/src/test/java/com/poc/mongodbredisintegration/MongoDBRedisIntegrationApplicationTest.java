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

package com.poc.mongodbredisintegration;

import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raja Kolli
 *
 */
public class MongoDBRedisIntegrationApplicationTest
		extends AbstractMongoDBRedisIntegrationTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoDBRedisIntegrationController controller;

	@Test
	public void contextLoads() {
		assertThat(this.mongoTemplate).isNotNull();
	}

	@Test
	public void insertData() {
		this.controller.deleteAll();
		final Book book = Book.builder().title("MongoDbCookBook")
				.text("MongoDB Data Book").author("Raja").build();
		final Book response = this.controller.saveBook(book);
		assertThat(response).isNotNull();
		assertThat(response.getBookId()).isNotBlank();
		assertThat(response.getAuthor()).isEqualTo("Raja");
		final Book updatedBook = this.controller.updateAuthorByTitle("MongoDbCookBook",
				"Raja1");
		assertThat(updatedBook.getAuthor()).isEqualTo("Raja1");
		this.controller.deleteBookByTitle("JUNITTitle");
		final Book updatedBook1 = this.controller.findBookByTitle("JUNITTitle");
		assertThat(updatedBook1).isNull();
		this.controller.deleteBookByTitle("MongoDbCookBook");
	}

}
