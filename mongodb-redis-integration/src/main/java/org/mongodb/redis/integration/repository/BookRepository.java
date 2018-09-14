package org.mongodb.redis.integration.repository;

import org.mongodb.redis.integration.document.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

	Book findBookByTitle(String titleName);

}
