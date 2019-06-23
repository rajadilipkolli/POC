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

package org.mongodb.redis.integration.handler;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mongodb.redis.integration.controller.BookController;
import org.mongodb.redis.integration.document.Book;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(properties = "spring.main.web-application-type=reactive", webEnvironment = WebEnvironment.RANDOM_PORT)
class BookHandlerTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private BookController controller;

	@BeforeAll
	void init() {
		if (this.controller.count() == 0) {
			this.controller.deleteCache();
			Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book").author("Raja").bookId("1")
					.build();
			this.controller.saveBook(book);
		}
	}

	@AfterAll
	void destroy() {
		this.controller.deleteAll();
	}

	@Test
	void testGetAll() {
		EntityExchangeResult<List<Book>> result = this.webTestClient.get().uri("/api/book")
				.accept(MediaType.APPLICATION_JSON).exchange().expectBodyList(Book.class).returnResult();
		assertThat(result.getResponseBody()).size().isGreaterThanOrEqualTo(1);
		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getUriTemplate()).isEqualTo("/api/book");

	}

	@Test
	void testGetBook() {
		Book response = this.webTestClient.get().uri("/api/book/{id}", 1).accept(MediaType.APPLICATION_JSON).exchange()
				.expectBody(Book.class).returnResult().getResponseBody();
		assertThat(response).isNotNull();
		assertThat(response.getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getAuthor()).isEqualTo("Raja");
		assertThat(response.getText()).isEqualTo("MongoDB Data Book");
	}

	@Test
	void testPostBook() {
		final Book book = Book.builder().author("Raja").text("This is a Test Book").title("JUNIT_TITLE").build();

		this.webTestClient.post().uri("/api/book/post").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(Mono.just(book), Book.class).exchange().expectStatus().isOk();
	}

	@Test
	void testPutBook() {
		Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book1").author("Raja").bookId("1")
				.version(0L).build();

		this.webTestClient.put().uri("/api/book/put/{id}", 1).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).body(Mono.just(book), Book.class).exchange().expectStatus().isOk();
	}

	@Test
	void testDeleteBook() {
		this.webTestClient.delete().uri("/api/book/delete/{id}", 1).exchange().expectStatus().isAccepted()
				.expectBody(String.class).isEqualTo("Delete Succesfully!");
	}

}
