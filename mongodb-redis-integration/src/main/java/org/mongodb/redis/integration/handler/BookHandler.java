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

package org.mongodb.redis.integration.handler;

import lombok.RequiredArgsConstructor;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.ReactiveBookRepository;
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

	private final ReactiveBookRepository bookReactiveRepository;

	/**
	 * GET ALL Books.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getAll() {
		// fetch all books from repository
		Flux<Book> books = this.bookReactiveRepository.findAll();

		// build response
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(books, Book.class);
	}

	/**
	 * GET a Book by ID.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> getBook(ServerRequest request) {
		// parse path-variable
		String bookId = request.pathVariable("id");

		// build notFound response
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();

		// get book from repository
		Mono<Book> bookMono = this.bookReactiveRepository.findById(bookId);

		// build response
		return bookMono.flatMap((Book book) -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(book))).switchIfEmpty(notFound);
	}

	/**
	 * POST a Book.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> postBook(ServerRequest request) {
		Mono<Book> monoBook = request.bodyToMono(Book.class);
		return ServerResponse.ok().build(monoBook.doOnNext(this.bookReactiveRepository::save).then());
	}

	/**
	 * PUT a Book.
	 * @param request a
	 * {@link org.springframework.web.reactive.function.server.ServerRequest} object.
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> putBook(ServerRequest request) {
		// parse id from path-variable
		String bookId = request.pathVariable("id");

		// get book data from request object
		Mono<Book> monoBook = request.bodyToMono(Book.class);
		monoBook.doOnNext((Book b) -> b.setBookId(bookId)).then();

		// get book from repository
		Mono<Book> responseMono = monoBook.doOnNext(this.bookReactiveRepository::save);

		// build response
		return responseMono.flatMap((Book book) -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(book)));
	}

	/**
	 * DELETE a Book.
	 * @param request a ServerRequest object
	 * @return a {@link reactor.core.publisher.Mono} object.
	 */
	public Mono<ServerResponse> deleteBook(ServerRequest request) {
		// parse id from path-variable
		String bookId = request.pathVariable("id");

		this.bookReactiveRepository.deleteById(bookId);
		// get book from repository
		Mono<String> responseMono = Mono.just("Delete Succesfully!");

		// build response
		return responseMono.flatMap((String strMono) -> ServerResponse.accepted().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromValue(strMono)));

	}

}
