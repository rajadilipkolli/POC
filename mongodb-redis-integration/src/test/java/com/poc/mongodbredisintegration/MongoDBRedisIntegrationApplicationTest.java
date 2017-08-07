/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;


public class MongoDBRedisIntegrationApplicationTest extends AbstractMongoDBRedisIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoDBRedisIntegrationController controller;

    @Test
    public void contextLoads() {
        assertThat(mongoTemplate).isNotNull();
    }

    @Test
    public void insertData() {
        controller.deleteAll();
        final Book book = new Book();
        book.setTitle("MongoDbCookBook");
        book.setText("MongoDB Data Book");
        book.setAuthor("Raja");
        final Book response = controller.saveBook(book);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getAuthor()).isEqualTo("Raja");
        final Book updatedBook = controller.updateAuthorByTitle("MongoDbCookBook", "Raja1");
        assertThat(updatedBook.getAuthor()).isEqualTo("Raja1");
        controller.deleteBookByTitle("JUNITTitle");
        final Book updatedBook1 = controller.findBookByTitle("JUNITTitle");
        assertThat(updatedBook1).isNull();
        controller.deleteBookByTitle("MongoDbCookBook");
    }

}
