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

package com.poc.mongodbredisintegration.service.impl;

import java.util.List;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookRepository;
import com.poc.mongodbredisintegration.service.MongoDBRedisIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MongoDBRedisIntegrationServiceImpl class.
 * </p>
 *
 * @author Raja Kolli
 * @since July 2017
 * @version 0 : 5
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MongoDBRedisIntegrationServiceImpl
		implements MongoDBRedisIntegrationService {

	private final BookRepository repository;

	private final MongoTemplate mongoTemplate;

	/** {@inheritDoc} */
	@Override
	public Long count() {
		return this.repository.count();
	}

	/** {@inheritDoc} */
	@Override
	public Book save(Book book) {
		log.info("Saving book :{}", book);
		return this.repository.save(book);
	}

	/** {@inheritDoc} */
	@Cacheable(value = "book", key = "#title", unless = "#result == null")
	@Override
	public Book findBookByTitle(String title) {
		log.info("Finding Book by Title :{}", title);
		return this.repository.findByTitle(title);
	}

	/** {@inheritDoc} */
	@CachePut(value = "book", key = "#title")
	@Override
	public Book updateByTitle(String title, String author) {
		log.info("Updating Book Author by Title :{} with {}", title, author);
		final Query query = new Query(Criteria.where("title").is(title));
		final Update update = new Update().set("author", author);
		return this.mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true).upsert(false), Book.class);
	}

	/** {@inheritDoc} */
	@CachePut(value = "book", key = "#title")
	@Override
	public void deleteBook(String title) {
		log.info("deleting Books by title :{}", title);
		this.repository.deleteByTitle(title);
	}

	/** {@inheritDoc} */
	@CacheEvict(value = "book", allEntries = true)
	@Override
	public void deleteAllCache() {
		log.info("Deleting Cache");
	}

	/** {@inheritDoc} */
	@CacheEvict(value = "book", allEntries = true)
	@Override
	public void deleteAllCollections() {
		this.repository.deleteAll();
	}

	/** {@inheritDoc} */
	@Override
	public void saveAllBooks(List<Book> bookList) {
		this.repository.saveAll(bookList);
	}

}
