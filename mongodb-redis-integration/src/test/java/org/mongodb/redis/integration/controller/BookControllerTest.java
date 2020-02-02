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

package org.mongodb.redis.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.exception.BookNotFoundException;
import org.mongodb.redis.integration.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BookService bookService;

	@Test
	void getBookShouldReturnBook() throws Exception {
		given(this.bookService.findBookByTitle(anyString())).willReturn(Book.builder().title("JUNIT_TITLE")
				.author("JUNIT_AUTHOR").bookId("JUNIT").text("JUNIT_TEXT").version(1L).build());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/book/findByTitle/JUNIT_TITLE")).andExpect(status().isOk())
				.andExpect(jsonPath("title").value("JUNIT_TITLE")).andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
	}

	@Test
	void getBookNotFound() throws Exception {
		given(this.bookService.findBookByTitle(anyString()))
				.willThrow(new BookNotFoundException("Book with Title Not Found"));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/book/findByTitle/MongodbBook"))
				.andExpect(status().isNotFound());
	}

	@Test
	void saveBookShouldReturnBook() throws Exception {
		Book book = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR").bookId("JUNIT").text("JUNIT_TEXT")
				.build();
		given(this.bookService.saveBook(ArgumentMatchers.any(Book.class))).willReturn(book);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/book/saveBook")
						.content(this.objectMapper.writeValueAsString(book)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("title").value("JUNIT_TITLE"))
				.andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
	}

	@Test
	void updateBookShouldReturnBook() throws Exception {
		Book book = Book.builder().title("MongoDbCookBook").author("JUNIT_AUTHOR").bookId("JUNIT").text("JUNIT_TEXT")
				.build();
		given(this.bookService.updateAuthorByTitle(ArgumentMatchers.eq("MongoDbCookBook"), anyString()))
				.willReturn(book);

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/book/updateByTitle/MongoDbCookBook/Raja1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("title").value("MongoDbCookBook"))
				.andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
	}

	@Test
	void deleteBookShouldReturnError() throws Exception {
		willThrow(new BookNotFoundException("Book with Title Not Found")).given(this.bookService)
				.deleteBook(anyString());

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/book/deleteByTitle/MongoDbCookBook"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteBookShouldReturnValue() throws Exception {
		willDoNothing().given(this.bookService).deleteBook(anyString());

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/book/deleteByTitle/MongoDbCookBook"))
				.andExpect(status().isAccepted());
	}

}
