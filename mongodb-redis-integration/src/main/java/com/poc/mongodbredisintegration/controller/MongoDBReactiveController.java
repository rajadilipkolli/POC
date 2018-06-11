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

package com.poc.mongodbredisintegration.controller;

import javax.validation.Valid;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.service.MongoDBReactiveService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MongoDB Reactive Controller.
 *
 * @author Raja Kolli
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class MongoDBReactiveController {

	private final MongoDBReactiveService reactiveService;

	@GetMapping
	public Flux<Book> getAllBooks() {
		return this.reactiveService.findAllBooks();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Book>> getBookById(@PathVariable("id") String bookId) {
		return this.reactiveService.getBookById(bookId);
	}

	@PostMapping
	public Mono<Book> createTweets(@Valid @RequestBody Book book) {
		return this.reactiveService.save(book);
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Book>> updateBook(@PathVariable("id") String bookId,
			@Valid @RequestBody Book book) {
		return this.reactiveService.updateBook(bookId, book);
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String bookId) {
		return this.reactiveService.deleteBook(bookId);
	}

	// Books are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Book> streamAllBooks() {
		return this.reactiveService.findAllBooks();
	}

}
