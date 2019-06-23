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

package org.mongodb.redis.integration.reactiveservice;

import lombok.RequiredArgsConstructor;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.ReactiveBookRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * MongoDB Redis Integration Book ServiceImpl class.
 *
 * @author Raja Kolli
 *
 */
@Service
@RequiredArgsConstructor
public class ReactiveBookServiceImpl implements ReactiveBookService {

	private final ReactiveBookRepository reactiveRepository;

	@Override
	public Mono<Book> findByTitle(String title) {
		return this.reactiveRepository.findByTitle(title);
	}

	@Override
	public Mono<ResponseEntity<Book>> updateBook(String bookId, Book book) {
		return this.reactiveRepository.findById(bookId).flatMap((Book existingBook) -> {
			existingBook.setText(book.getText());
			return this.reactiveRepository.save(existingBook);
		}).map((Book updatedBook) -> new ResponseEntity<>(updatedBook, HttpStatus.OK))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveRepository.findAll();
	}

	@Override
	public Mono<ResponseEntity<Book>> getBookById(String bookId) {
		return this.reactiveRepository.findById(bookId).map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Mono<Book> createBook(Book book) {
		return this.reactiveRepository.save(book);
	}

	@Override
	public Mono<ResponseEntity<Void>> deleteBook(String bookId) {
		return this.reactiveRepository.findById(bookId)
				.flatMap((Book existingBook) -> this.reactiveRepository.delete(existingBook)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
