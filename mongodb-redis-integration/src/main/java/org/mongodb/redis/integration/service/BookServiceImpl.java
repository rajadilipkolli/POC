package org.mongodb.redis.integration.service;

import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Override
	@Cacheable(value = "books", key = "#title", unless = "#result == null")
	public Book findBookByTitle(String title) {
		return this.bookRepository.findBookByTitle(title);
	}

}
