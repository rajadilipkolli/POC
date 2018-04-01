/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.service;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MongoDBReactiveServiceImpl implements MongoDBReactiveService {

    private final BookReactiveRepository reactiveRepository;

    @Override
    public Flux<Book> findAllBooks() {
        return reactiveRepository.findAll();
    }

    @Override
    public Mono<Book> save(Book book) {
        return reactiveRepository.save(book);
    }

    @Override
    public Mono<ResponseEntity<Book>> getBookById(String bookId) {
        return reactiveRepository.findById(bookId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Book>> updateBook(String bookId, @Valid Book book) {
        return reactiveRepository.findById(bookId).flatMap(existingBook -> {
            existingBook.setText(book.getText());
            return reactiveRepository.save(existingBook);
        }).map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBook(String bookId) {
        return reactiveRepository.findById(bookId)
                .flatMap(existingBook -> reactiveRepository.delete(existingBook)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
