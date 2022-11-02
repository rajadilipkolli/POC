/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.document.Book;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findBookByTitle(String titleName);
}
