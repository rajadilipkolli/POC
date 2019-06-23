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

package org.mongodb.redis.integration;

import org.junit.jupiter.api.Test;
import org.mongodb.redis.integration.document.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for simple App.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getBookByTitle_returnsBookDetails() throws Exception {
		// arrange
		Book book = Book.builder().title("MongoDbCookBook").author("Raja").build();
		ResponseEntity<Book> response = this.restTemplate.postForEntity("/book/saveBook", book, Book.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");

		// act
		response = this.restTemplate.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja");

		// act by Update
		response = this.restTemplate.exchange("/book/updateByTitle/MongoDbCookBook/Raja1", HttpMethod.PUT, null,
				Book.class);

		// assert After Update
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja1");

		// act by Delete
		ResponseEntity<String> resp = this.restTemplate.exchange("/book/deleteByTitle/MongoDbCookBook",
				HttpMethod.DELETE, null, String.class);
		assertThat(resp).isNotNull();
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(resp.getBody()).isEqualTo("Book with title MongoDbCookBook deleted");

		response = this.restTemplate.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		resp = this.restTemplate.getForEntity("/book/deleteCache", String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resp.getBody()).isEqualTo("Deleted Full Cache");
	}

}
