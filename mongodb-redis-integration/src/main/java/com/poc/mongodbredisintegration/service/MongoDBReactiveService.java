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

package com.poc.mongodbredisintegration.service;

import com.poc.mongodbredisintegration.document.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

/**
 * Interface for MongoDBReactiveService.
 *
 * @author Raja Kolli
 * @version 0 : 11
 */
public interface MongoDBReactiveService {

	/**
	 * <p>findAllBooks.</p>
	 *
	 * @return a {@link reactor.core.publisher.Flux} object.
	 */
	Flux<Book> findAllBooks();

	/**
	 * <p>save.</p>
	 *
	 * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	Mono<Book> save(Book book);

	/**
	 * <p>getBookById.</p>
	 *
	 * @param bookId a {@link java.lang.String} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	Mono<ResponseEntity<Book>> getBookById(String bookId);

	/**
	 * <p>updateBook.</p>
	 *
	 * @param bookId a {@link java.lang.String} object.
	 * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	Mono<ResponseEntity<Book>> updateBook(String bookId, Book book);

	/**
	 * <p>deleteBook.</p>
	 *
	 * @param bookId a {@link java.lang.String} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	Mono<ResponseEntity<Void>> deleteBook(String bookId);

}
