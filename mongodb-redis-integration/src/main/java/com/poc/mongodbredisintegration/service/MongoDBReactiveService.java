/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.service;

import org.springframework.http.ResponseEntity;

import com.poc.mongodbredisintegration.document.Book;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoDBReactiveService {

    Flux<Book> findAllBooks();

    Mono<Book> save(Book book);

    Mono<ResponseEntity<Book>> getBookById(String bookId);

    Mono<ResponseEntity<Book>> updateBook(String bookId, Book book);

    Mono<ResponseEntity<Void>> deleteBook(String bookId);

}
