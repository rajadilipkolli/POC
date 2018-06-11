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

	void deleteByTitle(String title);

}
