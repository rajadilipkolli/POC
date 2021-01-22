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

import com.mongodb.redis.integration.document.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * MongoDB Redis Integration Book Service interface.
 *
 * @author Raja Kolli
 *
 */
public interface ReactiveBookService {

	Mono<Book> findByTitle(String title);

	Mono<Book> updateBook(String bookId, Book book);

	Flux<Book> findAllBooks();

	Mono<Book> getBookById(String bookId);

	Mono<Book> createBook(Book book);

	Mono<Book> deleteBook(String bookId);

	Mono<Boolean> deleteAll();

}
