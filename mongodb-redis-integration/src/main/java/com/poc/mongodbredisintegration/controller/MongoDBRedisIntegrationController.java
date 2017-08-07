/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
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

/**
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 *
 */
@RestController
@RequestMapping(value = "/book")
@RequiredArgsConstructor
public class MongoDBRedisIntegrationController {

    private final MongoDBRedisIntegrationService service;

    /**
     * <p>saveBook.</p>
     *
     * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    @PostMapping(value = "/saveBook")
    public Book saveBook(Book book) {
        return service.save(book);
    }

    /**
     * <p>findBookByTitle.</p>
     * 
     * unless is specified to not cache null values
     *
     * @param title a {@link java.lang.String} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    @GetMapping(value = "/findByTitle/{title}")
    @Cacheable(value = "book", key = "#title", unless = "#result == null")
    public Book findBookByTitle(@PathVariable String title) {
        return service.findBookByTitle(title);
    }

    /**
     * <p>updateByTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @param author a {@link java.lang.String} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    @PutMapping(value = "/updateByTitle/{title}/{author}")
    @CachePut(value = "book", key = "#title")
    public Book updateAuthorByTitle(@PathVariable(value = "title") String title,
            @PathVariable(value = "author") String author) {
        return service.updateByTitle(title, author);
    }

    /**
     * <p>deleteBookByTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    @DeleteMapping(value = "/deleteByTitle/{title}")
    @CacheEvict(value = "book", key = "#title")
    public String deleteBookByTitle(@PathVariable(value = "title") String title) {
        final Book book = this.findBookByTitle(title);
        if (null != book) {
            this.service.deleteBook(book.getId());
            return "Book with title " + title + " deleted";
        } else {
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

    /**
     * <p>count.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long count() {
        return this.service.count();
    }

    /**
     * <p>deleteAll.</p>
     */
    public void deleteAll() {
        this.service.deleteAllCollections();
    }

}
