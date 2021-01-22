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

package com.mongodb.redis.integration.handler;

import java.net.URI;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.service.ReactiveBookService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Handler which handles Routes.
 *
 * @author Raja Kolli
 * @since 0.2.1
 */
@Component
@RequiredArgsConstructor
public class BookHandler {

	private final ReactiveBookService reactiveBookService;

	public Mono<ServerResponse> getAll() {
		return defaultReadResponse(this.reactiveBookService.findAllBooks());
	}

	public Mono<ServerResponse> getBook(ServerRequest request) {
		// build notFound response
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get book from repository
		Mono<Book> bookMono = this.reactiveBookService.getBookById(id(request));

		// build response
		return bookMono.flatMap((Book book) -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(book))).switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> postBook(ServerRequest request) {
		Flux<Book> flux = request.bodyToFlux(Book.class).flatMap(this.reactiveBookService::createBook);
		return defaultWriteResponse(flux);
	}

	public Mono<ServerResponse> putBook(ServerRequest request) {

		Flux<Book> id = request.bodyToFlux(Book.class)
				.flatMap(p -> this.reactiveBookService.updateBook(id(request), p));
		return defaultReadResponse(id);
	}

	public Mono<ServerResponse> deleteBook(ServerRequest request) {
		return ServerResponse //
				.accepted() //
				.contentType(MediaType.APPLICATION_JSON) //
				.body(this.reactiveBookService.deleteBook(id(request)), Book.class) //
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Book> books) {
		return Mono //
				.from(books) //
				.flatMap(book -> ServerResponse //
						.created(URI.create("/api/book/" + book.getBookId())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.build() //
				); //
	}

	private Mono<ServerResponse> defaultReadResponse(Publisher<Book> books) {
		return ServerResponse //
				.ok() //
				.contentType(MediaType.APPLICATION_JSON) //
				.body(books, Book.class) //
				.switchIfEmpty(ServerResponse.notFound().build()); //
	}

	private static String id(ServerRequest serverRequest) {
		// parse id from path-variable
		return serverRequest.pathVariable("id");
	}

}
