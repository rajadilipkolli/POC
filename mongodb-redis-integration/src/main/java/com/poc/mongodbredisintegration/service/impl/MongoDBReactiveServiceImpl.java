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

package com.poc.mongodbredisintegration.service.impl;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;
import com.poc.mongodbredisintegration.service.MongoDBReactiveService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service layer which interacts with reactive repository.
 *
 * @author Raja Kolli
 * @since 0.1.1
 */
@Service
@RequiredArgsConstructor
public class MongoDBReactiveServiceImpl implements MongoDBReactiveService {

	private final BookReactiveRepository reactiveRepository;

	/** {@inheritDoc} */
	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveRepository.findAll();
	}

	/** {@inheritDoc} */
	@Override
	public Mono<Book> save(Book book) {
		return this.reactiveRepository.save(book);
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Book>> getBookById(String bookId) {
		return this.reactiveRepository.findById(bookId).map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Book>> updateBook(String bookId, Book book) {
		return this.reactiveRepository.findById(bookId).flatMap((existingBook) -> {
			existingBook.setText(book.getText());
			return this.reactiveRepository.save(existingBook);
		}).map((updatedTweet) -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/** {@inheritDoc} */
	@Override
	public Mono<ResponseEntity<Void>> deleteBook(String bookId) {
		return this.reactiveRepository.findById(bookId)
				.flatMap((existingBook) -> this.reactiveRepository.delete(existingBook)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
