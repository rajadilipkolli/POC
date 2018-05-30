/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.service;

import javax.validation.Valid;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;
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
 *
 */
@Service
@RequiredArgsConstructor
public class MongoDBReactiveServiceImpl implements MongoDBReactiveService {

	private final BookReactiveRepository reactiveRepository;

	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveRepository.findAll();
	}

	@Override
	public Mono<Book> save(Book book) {
		return this.reactiveRepository.save(book);
	}

	@Override
	public Mono<ResponseEntity<Book>> getBookById(String bookId) {
		return this.reactiveRepository.findById(bookId).map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Mono<ResponseEntity<Book>> updateBook(String bookId, @Valid Book book) {
		return this.reactiveRepository.findById(bookId).flatMap((existingBook) -> {
			existingBook.setText(book.getText());
			return this.reactiveRepository.save(existingBook);
		}).map((updatedTweet) -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@Override
	public Mono<ResponseEntity<Void>> deleteBook(String bookId) {
		return this.reactiveRepository.findById(bookId)
				.flatMap((existingBook) -> this.reactiveRepository.delete(existingBook)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}
