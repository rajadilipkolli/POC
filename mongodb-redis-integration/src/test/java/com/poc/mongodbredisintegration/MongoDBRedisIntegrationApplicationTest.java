/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
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
		assertThat(response.getId()).isNotBlank();
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
