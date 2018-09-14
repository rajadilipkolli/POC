package org.mongodb.redis.integration.service;

import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;

	@Override
	public Book findBookByTitle(String titleName) {
		return this.bookRepository.findBookByTitle(titleName);
	}

}
