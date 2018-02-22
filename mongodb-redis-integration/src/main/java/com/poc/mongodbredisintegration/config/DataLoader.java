/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;

import lombok.RequiredArgsConstructor;

/**
 * <p>DataLoader class.</p>
 *
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final MongoDBRedisIntegrationController controller;

    /** {@inheritDoc} */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Long cnt = controller.count();
        if (cnt == 0) {
            controller.deleteCache();
            Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book")
                    .author("Raja").build();
            controller.saveBook(book);

            final List<Book> bookList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                book = new Book();
                book.setTitle(RandomStringUtils.randomAlphanumeric(20));
                book.setText(RandomStringUtils.randomAlphanumeric(30));
                bookList.add(book);
            }
            controller.saveAllBooks(bookList);
        }
    }
}
