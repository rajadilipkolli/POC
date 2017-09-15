/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.service;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>MongoDBRedisIntegrationServiceImpl class.</p>
 *
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MongoDBRedisIntegrationServiceImpl
        implements MongoDBRedisIntegrationService {

    private final BookRepository repository;
    private final MongoTemplate mongoTemplate;

    /** {@inheritDoc} */
    @Override
    public Long count() {
        return this.repository.count();
    }

    /** {@inheritDoc} */
    @Override
    public Book save(Book book) {
        log.info("Saving book :{}", book);
        return this.repository.save(book);
    }

    /** {@inheritDoc} */
    @Override
    public Book findBookByTitle(String title) {
        log.info("Finding Book by Title :{}", title);
        return this.repository.findByTitle(title);
    }

    /** {@inheritDoc} */
    @Override
    public Book updateByTitle(String title, String author) {
        log.info("Updating Book Author by Title :{} with {}", title, author);
        final Query query = new Query(Criteria.where("title").is(title));
        final Update update = new Update().set("author", author);
        return this.mongoTemplate.findAndModify(query, update,
                new FindAndModifyOptions().returnNew(true).upsert(false), Book.class);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteBook(String id) {
        log.info("deleting Books by id :{}", id);
        this.repository.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAllCache() {
        log.info("Deleting Cache");
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAllCollections() {
        this.repository.deleteAll();
    }

}
