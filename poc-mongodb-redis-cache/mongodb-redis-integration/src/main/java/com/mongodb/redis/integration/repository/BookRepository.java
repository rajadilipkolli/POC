/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.document.Book;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findBookByTitle(String titleName);
}
