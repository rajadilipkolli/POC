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

package com.poc.mongodbredisintegration.service;

import java.util.List;

import com.poc.mongodbredisintegration.document.Book;

/**
 * <p>MongoDBRedisIntegrationService interface.</p>
 *
 * @author Raja Kolli
 * @since July 2017
 * @version 0 : 5
 */
public interface MongoDBRedisIntegrationService {

	/**
	 * <p>count.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	Long count();

	/**
	 * <p>save.</p>
	 *
	 * @param book a {@link com.poc.mongodbredisintegration.document.Book} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	Book save(Book book);

	/**
	 * <p>findBookByTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	Book findBookByTitle(String title);

	/**
	 * <p>updateByTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @param author a {@link java.lang.String} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	Book updateByTitle(String title, String author);

	/**
	 * <p>deleteBook.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	void deleteBook(String id);

	/**
	 * <p>deleteAllCache.</p>
	 */
	void deleteAllCache();

	/**
	 * <p>deleteAllCollections.</p>
	 */
	void deleteAllCollections();

	/**
	 * <p>saveAllBooks.</p>
	 *
	 * @param bookList a {@link java.util.List} object.
	 */
	void saveAllBooks(List<Book> bookList);

}
