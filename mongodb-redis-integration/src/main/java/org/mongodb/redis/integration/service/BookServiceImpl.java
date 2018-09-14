package org.mongodb.redis.integration.service;

import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Override
	@Cacheable(value = "books", key = "#title", unless = "#result == null")
	public Book findBookByTitle(String title) {
		log.info("Finding Book by Title :{}", title);
		return this.bookRepository.findBookByTitle(title);
	}

	@Override
	public Book saveBook(Book book) {
		log.info("Saving book :{}", book.toString());
		return this.bookRepository.save(book);
	}

}
