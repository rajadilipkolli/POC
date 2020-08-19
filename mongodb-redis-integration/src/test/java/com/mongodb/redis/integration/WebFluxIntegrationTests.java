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
package com.mongodb.redis.integration;

import java.util.Collections;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class WebFluxIntegrationTests {

	private static final String TITLE = "JUNIT_TITLE";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ReactiveMongoOperations operations;

	@Autowired
	private ReactiveBookRepository bookReactiveRepository;

	@BeforeAll
	void setUp() {
		if (!this.operations.collectionExists(Book.class).block()) {
			this.operations.createCollection(Book.class,
					CollectionOptions.empty().size(1024 * 1024).maxDocuments(100).capped()).then().block();
		}
		this.bookReactiveRepository.save(
				Book.builder().title("MongoDbCookBook").text("MongoDB Data Book").author("Raja").bookId("1").build())
				.then().block();
	}

	@AfterAll
	void tearDown() {
		this.operations.dropCollection(Book.class).block();
	}

	@Test
	void getBook_WithName_ReturnsBook() {
		Book book = this.webTestClient.get().uri("/books/title/{title}", "MongoDbCookBook").exchange().expectStatus()
				.isOk().expectBody(Book.class).returnResult().getResponseBody();
		assertThat(book).isNotNull();
		assertThat(book.getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(book.getAuthor()).isEqualTo("Raja");
	}

	@Test
	void testCreateBook() {
		final Book book = Book.builder().author("Raja").text("This is a Test Book").title(TITLE).build();

		this.webTestClient.post().uri("/books").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(book)).exchange().expectStatus()
				.isCreated().expectHeader().contentType(MediaType.APPLICATION_JSON).expectHeader().exists("location")
				.expectBody().jsonPath("$.bookId").isNotEmpty().jsonPath("$.text").isEqualTo("This is a Test Book")
				.jsonPath("$.title").isEqualTo(TITLE);
	}

	@Test
	@DisplayName("Invalid Data")
	void testCreateBookFail() {
		Book book = Book.builder().author("Raja").text("This is a Test Book")
				.title(RandomStringUtils.randomAlphanumeric(200)).build();

		this.webTestClient.post().uri("/books").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(book)).exchange().expectStatus()
				.isBadRequest().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.message").isNotEmpty().jsonPath("$.errors.[0].defaultMessage")
				.isEqualTo("size must be between 0 and 140");
	}

	@Test
	void testGetAllBooks() {
		this.webTestClient.get().uri("/books").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON).expectBodyList(Book.class);
	}

	@Test
	void testGetSingleBook() {
		final Book book = this.bookReactiveRepository
				.save(Book.builder().author("Raja").text("This is a Test Book").title(TITLE).build()).block();

		this.webTestClient.get().uri("/books/{id}", Collections.singletonMap("id", book.getBookId())).exchange()
				.expectStatus().isOk().expectBody()
				.consumeWith((response) -> assertThat(response.getResponseBody()).isNotNull());
	}

	@Test
	void testUpdateBook() {
		final Book book = this.bookReactiveRepository
				.save(Book.builder().author("Raja").text("This is a Test Book").title(TITLE).build()).block();

		final Book newBookData = Book.builder().author("Raja").text("Updated Book").title(TITLE).build();

		this.webTestClient.put().uri("/books/{id}", Collections.singletonMap("id", book.getBookId()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(newBookData), Book.class).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.text").isEqualTo("Updated Book");
	}

	@Test
	void testDeleteBook() {
		final Book book = this.bookReactiveRepository
				.save(Book.builder().author("Raja").text("This is a Test Book").title(TITLE).build()).block();

		this.webTestClient.delete().uri("/books/{id}", Collections.singletonMap("id", book.getBookId())).exchange()
				.expectStatus().isAccepted();

		this.webTestClient.get().uri("/books/{id}", Collections.singletonMap("id", book.getBookId())).exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void deleteCache() {
		byte[] response = this.webTestClient.delete().uri("/books/").exchange().expectStatus().isOk().expectBody()
				.returnResult().getResponseBody();

		assertThat(response).isNotNull();
		assertThat(new String(response)).isEqualTo("true");
	}

	@Test
	@DisplayName("Test case for EventStream Value")
	void testTextEventStreamValue() {
		this.webTestClient.get().uri("/books/stream").exchange().expectStatus().is2xxSuccessful().expectHeader()
				.contentType("text/event-stream;charset=UTF-8").expectBodyList(Book.class);
	}

}
