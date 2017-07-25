package com.poc.mongodbredisintegration.service;

import com.poc.mongodbredisintegration.document.Book;

public interface MongoDBRedisIntegrationService {

    Long count();

    Book save(Book book);

    Book findBookByTitle(String title);

    Book updateByTitle(String title, String author);

    void deleteBook(String id);

    void deleteAllCache();

    void deleteAllCollections();

}
