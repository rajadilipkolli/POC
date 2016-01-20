package com.example;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String>
{
    Stream<Book> findByTitle(String title);
}
