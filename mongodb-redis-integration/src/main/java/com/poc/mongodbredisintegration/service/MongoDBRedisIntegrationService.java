/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.service;

import java.util.List;

import com.poc.mongodbredisintegration.document.Book;

/**
 * <p>MongoDBRedisIntegrationService interface.</p>
 *
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 */
public interface MongoDBRedisIntegrationService {

    /**
     * <p>count.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    Long count();

    /**
     * <p>save.</p>
     *
     * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    Book save(Book book);

    /**
     * <p>findBookByTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    Book findBookByTitle(String title);

    /**
     * <p>updateByTitle.</p>
     *
     * @param title a {@link java.lang.String} object.
     * @param author a {@link java.lang.String} object.
     * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
     */
    Book updateByTitle(String title, String author);

    /**
     * <p>deleteBook.</p>
     *
     * @param id a {@link java.lang.String} object.
     */
    void deleteBook(String id);

    /**
     * <p>deleteAllCache.</p>
     */
    void deleteAllCache();

    /**
     * <p>deleteAllCollections.</p>
     */
    void deleteAllCollections();

    void saveAllBooks(List<Book> bookList);

}
