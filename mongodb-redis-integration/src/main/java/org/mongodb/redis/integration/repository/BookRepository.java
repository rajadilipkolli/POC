package org.mongodb.redis.integration.repository;

import java.util.Optional;

import org.mongodb.redis.integration.document.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

	Optional<Book> findBookByTitle(String titleName);

}
