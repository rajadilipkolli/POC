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

package com.poc.mongodbredisintegration.handler;

import java.util.ArrayList;
import java.util.List;

import com.poc.mongodbredisintegration.AbstractMongoDBRedisIntegrationTest;
import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Raja Kolli
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class BookHandlerTest extends AbstractMongoDBRedisIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private MongoDBRedisIntegrationController controller;

	@BeforeAll
	void init() {

		if (this.controller.count() == 0) {
			this.controller.deleteCache();
			Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book")
					.author("Raja").bookId("1").build();
			this.controller.saveBook(book);

			final List<Book> bookList = new ArrayList<>();
			for (int i = 0; i < 100; i++) {
				book = new Book();
				book.setTitle(RandomStringUtils.randomAlphanumeric(20));
				book.setText(RandomStringUtils.randomAlphanumeric(30));
				bookList.add(book);
			}
			this.controller.saveAllBooks(bookList);
		}
	}

	@Test
	void testGetAll() {
		this.webTestClient.get().uri("/api/book").accept(MediaType.APPLICATION_JSON)
				.exchange().expectBodyList(Book.class).hasSize(101);
	}

	@Test
	void testGetBook() {
		Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book")
				.author("Raja").bookId("1").version(0L).build();

		this.webTestClient.get().uri("/api/book/{id}", 1)
				.accept(MediaType.APPLICATION_JSON).exchange().expectBody(Book.class)
				.isEqualTo(book);

	}

	@Test
	void testPostBook() {
		final Book book = Book.builder().author("Raja").text("This is a Test Book")
				.title("JUNIT_TITLE").build();

		this.webTestClient.post().uri("/api/book/post")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(Mono.just(book), Book.class)
				.exchange().expectStatus().isOk();
	}

	@Test
	void testPutBook() {
		Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book1")
				.author("Raja").bookId("1").version(0L).build();

		this.webTestClient.put().uri("/api/book/put/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(Mono.just(book), Book.class)
				.exchange().expectStatus().isOk();
	}

	@Test
	void testDeleteBook() {
		this.webTestClient.delete().uri("/api/book/delete/{id}", 1).exchange()
				.expectStatus().isAccepted().expectBody(String.class)
				.isEqualTo("Delete Succesfully!");
	}

}
