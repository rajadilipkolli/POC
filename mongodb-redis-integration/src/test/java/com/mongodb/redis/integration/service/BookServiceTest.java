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
package com.mongodb.redis.integration.service;

import java.util.Optional;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.BookRepository;
import com.mongodb.redis.integration.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@TestInstance(Lifecycle.PER_CLASS)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	private BookService bookService;

	@BeforeAll
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.bookService = new BookServiceImpl(this.bookRepository, null);
	}

	@Test
	void getBookDetailsReturnBookInfo() throws BookNotFoundException {
		Book builderBook = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR").bookId("JUNIT").text("JUNIT_TEXT")
				.version(1L).build();
		given(this.bookRepository.findBookByTitle(eq("JUNIT_TITLE"))).willReturn(Optional.of(builderBook));

		Book book = this.bookService.findBookByTitle("JUNIT_TITLE");

		assertThat(book.getTitle()).isEqualTo("JUNIT_TITLE");
		assertThat(book.getAuthor()).isEqualTo("JUNIT_AUTHOR");
		assertThat(book.getText()).isEqualTo("JUNIT_TEXT");
	}

	@Test
	void getBookDetailsWhenBookNotFound() {
		given(this.bookRepository.findBookByTitle(eq("prius"))).willReturn(Optional.empty());
		try {
			this.bookService.findBookByTitle("prius");
		}
		catch (BookNotFoundException ex) {
			assertThat(ex.getMessage()).isEqualTo("Book with Title prius NotFound!");
			assertThatExceptionOfType(BookNotFoundException.class).isThrownBy(() -> {
				throw new BookNotFoundException("Book with Title prius NotFound!");
			}).withMessage("%s!", "Book with Title prius NotFound").withNoCause();
		}

	}

}
