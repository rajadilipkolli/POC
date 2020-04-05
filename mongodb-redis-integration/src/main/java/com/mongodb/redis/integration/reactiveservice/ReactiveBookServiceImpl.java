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

package com.mongodb.redis.integration.reactiveservice;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.reactiveevent.BookCreatedEvent;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.ApplicationEventPublisher;
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

	private final ApplicationEventPublisher publisher;

	@Override
	public Mono<Book> findByTitle(String title) {
		return this.reactiveRepository.findByTitle(title);
	}

	@Override
	public Mono<Book> updateBook(String bookId, Book requestedBook) {
		return this.reactiveRepository //
				.findById(bookId) //
				.map(persistedBook -> {
					persistedBook.setAuthor(requestedBook.getAuthor());
					persistedBook.setText(requestedBook.getText());
					persistedBook.setTitle(requestedBook.getTitle());
					return persistedBook;
				}) //
				.flatMap(this.reactiveRepository::save); //
	}

	@Override
	public Flux<Book> findAllBooks() {
		return this.reactiveRepository.findAll();
	}

	@Override
	public Mono<Book> getBookById(String bookId) {
		return this.reactiveRepository.findById(bookId);
	}

	@Override
	public Mono<Book> createBook(Book book) {
		return this.reactiveRepository.save(book) //
				.doOnSuccess(persistedBook -> this.publisher.publishEvent(new BookCreatedEvent(persistedBook)));
	}

	@Override
	public Mono<Book> deleteBook(String bookId) {
		return this.reactiveRepository //
				.findById(bookId) //
				.flatMap(book -> this.reactiveRepository.deleteById(book.getBookId()).thenReturn(book)); //
	}

}
