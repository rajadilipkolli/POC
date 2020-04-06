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
package com.mongodb.redis.integration.service.impl;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.reactiveevent.BookCreatedEvent;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import com.mongodb.redis.integration.service.ReactiveBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * MongoDB Redis Integration Book ServiceImpl class.
 *
 * @author Raja Kolli
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveBookServiceImpl implements ReactiveBookService {

	/**
	 * KeyName of the region where cache stored.
	 */
	public static final String CACHEABLES_REGION_KEY = "reactivebooks";

	private final ReactiveBookRepository reactiveRepository;

	private final ApplicationEventPublisher publisher;

	private final ReactiveRedisTemplate<String, Book> reactiveJsonBookRedisTemplate;

	@Override
	public Mono<Book> findByTitle(String title) {
		return this.reactiveRepository.findByTitle(title).log();
	}

	@Override
	public Mono<Book> updateBook(String bookId, Book requestedBook) {
		return this.reactiveRepository //
				.findById(bookId) //
				.log() //
				.map(persistedBook -> {
					persistedBook.setAuthor(requestedBook.getAuthor());
					persistedBook.setText(requestedBook.getText());
					persistedBook.setTitle(requestedBook.getTitle());
					return persistedBook;
				}) //
				.flatMap(this.reactiveRepository::save).flatMap(book -> this.reactiveJsonBookRedisTemplate.opsForHash()
						.put(CACHEABLES_REGION_KEY, book.getBookId(), book).log("Pushing to Cache").map(o -> book));
	}

	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveJsonBookRedisTemplate.<String, Book>opsForHash().values(CACHEABLES_REGION_KEY)
				.switchIfEmpty(this.reactiveRepository.findAll().log("Fetching from Database")
						.flatMap(books -> this.reactiveJsonBookRedisTemplate.opsForHash()
								.put(CACHEABLES_REGION_KEY, books.getBookId(), books).log("Pushing to Cache")
								.map(o -> books)));
	}

	@Override
	public Mono<Book> getBookById(String bookId) {
		return this.reactiveJsonBookRedisTemplate.<String, Book>opsForHash().get(CACHEABLES_REGION_KEY, bookId)
				.log("Fetching from cache")
				.switchIfEmpty(this.reactiveRepository.findById(bookId).log("Fetching from Database")
						.flatMap(book -> this.reactiveJsonBookRedisTemplate.opsForHash()
								.put(CACHEABLES_REGION_KEY, bookId, book).log("Pushing to Cache").map(o -> book)));
	}

	@Override
	public Mono<Book> createBook(Book bookToPersist) {
		return this.reactiveRepository.save(bookToPersist).log("Saving to DB")
				.flatMap(book -> this.reactiveJsonBookRedisTemplate.opsForHash()
						.put(CACHEABLES_REGION_KEY, book.getBookId(), book).log("Pushing to Cache").map(o -> book))
				.doOnSuccess(persistedBook -> this.publisher.publishEvent(new BookCreatedEvent(persistedBook)))
				.doOnError(error -> log.error("The following error happened on processFoo method!", error));
	}

	@Override
	public Mono<Book> deleteBook(String bookId) {
		return this.reactiveRepository //
				.findById(bookId) //
				.flatMap(book -> this.reactiveRepository.deleteById(book.getBookId()).log().thenReturn(book))
				.flatMap(returnedBook -> this.reactiveJsonBookRedisTemplate.opsForHash()
						.remove(CACHEABLES_REGION_KEY, bookId).log("Deleting From Cache").map(o -> returnedBook));
	}

	@Override
	public Mono<Boolean> deleteAll() {
		return this.reactiveJsonBookRedisTemplate.opsForHash().delete(CACHEABLES_REGION_KEY).log("Deleting All Cache");
	}

}
