package com.poc.mongodbredisintegration.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.poc.mongodbredisintegration.document.Book;

public interface BookRepository extends MongoRepository<Book, String> {
    
    Book findByTitle(String title);

    Optional<Book> findById(String id);
}
