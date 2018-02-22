/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.poc.mongodbredisintegration.document.Book;

public interface BookReactiveRepository extends ReactiveMongoRepository<Book, String> {

}
