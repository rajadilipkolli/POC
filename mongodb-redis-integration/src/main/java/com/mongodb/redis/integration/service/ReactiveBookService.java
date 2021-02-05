package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public record ReactiveBookService(ReactiveBookRepository reactiveBookRepository) {

  public Mono<Book> findBookByTitle(String title) {
    return reactiveBookRepository
        .findByTitle(title)
        .switchIfEmpty(
            Mono.error(new BookNotFoundException("Book with Title " + title + " NotFound!")));
  }
}
