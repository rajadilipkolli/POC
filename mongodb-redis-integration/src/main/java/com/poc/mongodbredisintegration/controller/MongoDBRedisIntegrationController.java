/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.mongodbredisintegration.controller;

import java.util.List;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.service.MongoDBRedisIntegrationService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MongoDBRedisIntegrationController class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0 : 5
 * @since July 2017
 */
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class MongoDBRedisIntegrationController {

	private final MongoDBRedisIntegrationService service;

	/**
	 * <p>saveBook.</p>
	 *
	 * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	@PostMapping("/saveBook")
	public Book saveBook(Book book) {
		return this.service.save(book);
	}

	/**
	 * <p>findBookByTitle.</p>
	 *
	 * unless is specified to not cache null values
	 *
	 * @param title a {@link java.lang.String} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	@GetMapping("/findByTitle/{title}")
	public Book findBookByTitle(@PathVariable String title) {
		return this.service.findBookByTitle(title);
	}

	/**
	 * <p>updateByTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @param author a {@link java.lang.String} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	@PutMapping("/updateByTitle/{title}/{author}")
	public Book updateAuthorByTitle(@PathVariable("title") String title,
			@PathVariable("author") String author) {
		return this.service.updateByTitle(title, author);
	}

	/**
	 * <p>deleteBookByTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	@DeleteMapping("/deleteByTitle/{title}")
	public String deleteBookByTitle(@PathVariable("title") String title) {
		final Book book = this.findBookByTitle(title);
		if (null != book) {
			this.service.deleteBook(title);
			return "Book with title " + title + " deleted";
		}
		else {
			return "Book with title " + title + " Not Found";
		}
	}

	/**
	 * Deletes all cache.
	 */
	@GetMapping("/deleteCache")
	public void deleteCache() {
		this.service.deleteAllCache();
	}

	/**
	 * count.
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long count() {
		return this.service.count();
	}

	/**
	 * <p>deleteAll.</p>
	 */
	public void deleteAll() {
		this.service.deleteAllCollections();
	}

	/**
	 * <p>saveAllBooks.</p>
	 *
	 * @param bookList a {@link java.util.List} object.
	 */
	public void saveAllBooks(List<Book> bookList) {
		this.service.saveAllBooks(bookList);
	}

}
