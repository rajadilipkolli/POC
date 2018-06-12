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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Raja Kolli
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
public class WebfluxDemoApplicationTests extends AbstractMongoDBRedisIntegrationTest {

	private static final String TITLE = "JUNIT_TITLE";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private BookReactiveRepository bookReactiveRepository;

	@BeforeAll
	public void setUp() {
		final List<Book> bookList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			final Book book = new Book();
			book.setTitle(RandomStringUtils.randomAlphabetic(10));
			bookList.add(book);
		}
		this.bookReactiveRepository.saveAll(bookList);
	}

	@Test
	public void testCreateBook() {
		final Book book = Book.builder().author("Raja").text("This is a Test Book")
				.title(TITLE).build();

		this.webTestClient.post().uri("/books")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(book), Book.class)
				.exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.bookId").isNotEmpty().jsonPath("$.text")
				.isEqualTo("This is a Test Book").jsonPath("$.title").isEqualTo(TITLE);
	}

	@Test
	@DisplayName("Invalid Data")
	public void testCreateBookFail() {
		Book book = Book.builder().author("Raja").text("This is a Test Book")
				.title(RandomStringUtils.randomAlphanumeric(200)).build();

		this.webTestClient.post().uri("/books")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(BodyInserters.fromObject(book)).exchange().expectStatus()
				.isBadRequest().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.message")
				.isEqualTo("Validation failed for object='book'. Error count: 1")
				.jsonPath("$.errors.[0].defaultMessage")
				.isEqualTo("size must be between 0 and 140");

		book = Book.builder().build();
		this.webTestClient.post().uri("/books")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(book), Book.class)
				.exchange().expectStatus().isBadRequest().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.message")
				.isEqualTo("Validation failed for object='book'. Error count: 1")
				.jsonPath("$.errors.[0].defaultMessage").isEqualTo("must not be blank");
	}

	@Test
	public void testGetAllBooks() {
		this.webTestClient.get().uri("/books").accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Book.class);
	}

	@Test
	public void testGetSingleBook() {
		final Book book = this.bookReactiveRepository.save(Book.builder().author("Raja")
				.text("This is a Test Book").title(TITLE).build()).block();

		this.webTestClient.get()
				.uri("/books/{id}", Collections.singletonMap("id", book.getBookId()))
				.exchange().expectStatus().isOk().expectBody().consumeWith(
						(response) -> assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	public void testUpdateBook() {
		final Book book = this.bookReactiveRepository.save(Book.builder().author("Raja")
				.text("This is a Test Book").title(TITLE).build()).block();

		final Book newBookData = Book.builder().author("Raja").text("Updated Book")
				.title(TITLE).build();

		this.webTestClient.put()
				.uri("/books/{id}", Collections.singletonMap("id", book.getBookId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(newBookData), Book.class).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
				.jsonPath("$.text").isEqualTo("Updated Book");
	}

	@Test
	public void testDeleteBook() {
		final Book book = this.bookReactiveRepository.save(Book.builder().author("Raja")
				.text("This is a Test Book").title(TITLE).build()).block();

		this.webTestClient.delete()
				.uri("/books/{id}", Collections.singletonMap("id", book.getBookId()))
				.exchange().expectStatus().isOk();

		this.webTestClient.get()
				.uri("/books/{id}", Collections.singletonMap("id", book.getBookId()))
				.exchange().expectStatus().isNotFound();
	}

	@Test
	public void testActuatorStatus() {
		this.webTestClient.get().uri("/actuator/health")
				.accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectBody().json("{\"status\":\"UP\"}");
	}

	@Test
	@DisplayName("Test case for EventStream Value")
	public void testTextEventStreamValue() {
		this.webTestClient.get().uri("/books/stream").exchange().expectStatus()
				.is2xxSuccessful().expectHeader()
				.contentType("text/event-stream;charset=UTF-8")
				.expectBodyList(Book.class);
	}

}
