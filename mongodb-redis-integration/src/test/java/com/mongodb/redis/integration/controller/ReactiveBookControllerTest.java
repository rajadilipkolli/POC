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

package com.mongodb.redis.integration.controller;

import com.mongodb.redis.integration.config.BookCreatedEventPublisher;
import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.service.ReactiveBookService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = ReactiveBookController.class)
public class ReactiveBookControllerTest {

	@MockBean
	private ReactiveBookService reactiveBookService;

	@MockBean
	private BookCreatedEventPublisher eventPublisher;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void getBook_WithName_returnsBook() {
		Book book = Book.builder().title("prius").author("hybrid").build();
		given(this.reactiveBookService.findByTitle("prius")).willReturn(Mono.just(book));

		this.webTestClient.get().uri("/books/title/{title}", "prius").exchange().expectStatus().isOk().expectBody()
				.jsonPath("title").isEqualTo("prius").jsonPath("author").isEqualTo("hybrid");
	}

}
