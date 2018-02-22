package com.poc.mongodbredisintegration.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.poc.mongodbredisintegration.document.Book;

public interface BookReactiveRepository extends ReactiveMongoRepository<Book, String> {

}
