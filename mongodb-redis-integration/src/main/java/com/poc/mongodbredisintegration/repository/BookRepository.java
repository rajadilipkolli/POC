/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.repository;

import java.util.Optional;

import com.poc.mongodbredisintegration.document.Book;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * <p>
 * BookRepository interface.
 * </p>
 *
 * @author Raja Kolli
 * @since July 2017
 * @version 0 : 5
 */
public interface BookRepository extends MongoRepository<Book, String> {

	/**
	 * <p>
	 * findByTitle.
	 * </p>
	 * @param title a {@link java.lang.String} object.
	 * @return a {@link com.poc.mongodbredisintegration.document.Book} object.
	 */
	Book findByTitle(String title);

	/**
	 * <p>
	 * findById.
	 * </p>
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.util.Optional} object.
	 */
	Optional<Book> findById(String id);

}
