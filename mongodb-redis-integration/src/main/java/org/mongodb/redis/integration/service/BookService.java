package org.mongodb.redis.integration.service;

import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.exception.BookNotFoundException;

public interface BookService {

	Book findBookByTitle(String titleName) throws BookNotFoundException;

	Book saveBook(Book book);

	Book updateAuthorByTitle(String title, String author);

	void deleteBook(String title) throws BookNotFoundException;

	String deleteAllCache();

}
