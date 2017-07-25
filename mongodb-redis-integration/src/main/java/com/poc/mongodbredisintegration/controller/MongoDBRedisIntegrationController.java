package com.poc.mongodbredisintegration.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.service.MongoDBRedisIntegrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/book")
@RequiredArgsConstructor
public class MongoDBRedisIntegrationController {

    private final MongoDBRedisIntegrationService service;

    @PostMapping(value = "/saveBook")
    public Book saveBook(Book book) {
        return service.save(book);
    }

    /**
     * unless is specified to not cache null values
     * 
     * @param title
     * @return
     */
    @GetMapping(value = "/findByTitle/{title}")
    @Cacheable(value = "book", key = "#title", unless = "#result == null")
    public Book findBookByTitle(@PathVariable String title) {
        return service.findBookByTitle(title);
    }

    @PutMapping(value = "/updateByTitle/{title}/{author}")
    @CachePut(value = "book", key = "#title")
    public Book updateByTitle(@PathVariable(value = "title") String title,
            @PathVariable(value = "author") String author) {
        return service.updateByTitle(title, author);
    }

    @DeleteMapping(value = "/deleteByTitle/{title}")
    @CacheEvict(value = "book", key = "#title")
    public String deleteBookByTitle(@PathVariable(value = "title") String title) {
        Book book = this.findBookByTitle(title);
        if (null != book) {
            this.service.deleteBook(book.getId());
            return "Book with title " + title + " deleted";
        }
        else {
            return "Book with title " + title + " Not Found";
        }
    }

    /**
     * Deletes all cache
     */
    @GetMapping(value = "/deleteCache")
    @CacheEvict(value = "book", allEntries = true)
    public void deleteCache() {
        this.service.deleteAllCache();
    }

    public Long count() {
        return this.service.count();
    }

    public void deleteAll() {
        this.service.deleteAllCollections();
    }

}
