package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.document.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReactiveBookRepository extends ReactiveMongoRepository<Book, String> {

  Mono<Book> findByTitle(String bookTitle);
}
