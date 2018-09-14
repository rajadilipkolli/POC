package org.mongodb.redis.integration.service;

import org.mongodb.redis.integration.document.Book;

public interface BookService {

	Book findBookByTitle(String titleName);

	Book saveBook(Book book);

}
