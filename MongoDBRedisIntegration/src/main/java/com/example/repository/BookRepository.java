package com.example.repository;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.model.Book;

public interface BookRepository extends MongoRepository<Book, String>
{
    List<Book> findByTitle(String title);

    @CacheEvict(value = "book", key = "#title")
    void delete(String title);
}
