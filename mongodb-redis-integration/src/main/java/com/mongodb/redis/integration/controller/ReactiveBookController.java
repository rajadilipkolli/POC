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

import java.net.URI;

import javax.validation.Valid;

import com.mongodb.redis.integration.config.BookCreatedEventPublisher;
import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.reactiveevent.BookCreatedEvent;
import com.mongodb.redis.integration.reactiveservice.ReactiveBookService;
import org.reactivestreams.Publisher;
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
 * MongoDB Redis Integration Reactive Controller Book class.
 *
 * @author Raja Kolli
 */
@RestController
@RequestMapping("/books")
public class ReactiveBookController {

	private final ReactiveBookService reactiveBookService;

	private final Flux<BookCreatedEvent> events;

	public ReactiveBookController(BookCreatedEventPublisher eventPublisher, ReactiveBookService reactiveBookService) {
		this.events = Flux.create(eventPublisher).share();
		this.reactiveBookService = reactiveBookService;
	}

	@GetMapping("/title/{title}")
	public Publisher<Book> getBookByTitle(@PathVariable(name = "title") String title) {
		return this.reactiveBookService.findByTitle(title);
	}

	@GetMapping
	public Publisher<Book> getAllBooks() {
		return this.reactiveBookService.findAllBooks();
	}

	@GetMapping("/{id}")
	public Publisher<ResponseEntity<Book>> getBookById(@PathVariable("id") String bookId) {
		return this.reactiveBookService.getBookById(bookId).map(ResponseEntity::ok)
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	@PostMapping
	public Publisher<ResponseEntity<Book>> createBook(@Valid @RequestBody Book book) {
		return this.reactiveBookService.createBook(book) //
				.map(book1 -> ResponseEntity.created(URI.create("/books/" + book1.getBookId())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.body(book1));
	}

	@PutMapping("/{id}")
	public Publisher<ResponseEntity<Book>> updateBook(@PathVariable("id") String bookId,
			@Valid @RequestBody Book book) {
		return Mono //
				.just(book) //
				.flatMap(book1 -> this.reactiveBookService.updateBook(bookId, book1)) //
				.map(updatedBook -> ResponseEntity //
						.ok() //
						.contentType(MediaType.APPLICATION_JSON) //
						.body(updatedBook));
	}

	@DeleteMapping("/{id}")
	public Publisher<ResponseEntity<Object>> deleteBook(@PathVariable("id") String bookId) {
		return this.reactiveBookService.deleteBook(bookId) //
				.map(book -> ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).build()) //
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	// Books are Sent to the client as Server Sent Events
	@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Book> streamAllBooks() {
		// this.events.map(EventObject::getSource);
		return this.reactiveBookService.findAllBooks();
	}

}
