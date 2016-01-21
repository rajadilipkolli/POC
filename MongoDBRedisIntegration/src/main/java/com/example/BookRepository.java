package com.example;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String>
{
    List<Book> findByTitle(String title);
    
    @CacheEvict(value = "book", key = "#title")
    public void delete(String title);
}
