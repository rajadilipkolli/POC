/*
 * Copyright 2017 the original author or authors.
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
package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.model.Book;
import com.example.repository.BookRepository;

/**
 * @author rajakolli
 *
 */
@Service
public class WebServicesService {

	@Autowired
	private BookRepository repository;

	/*
	 * @Autowired private RedisTemplate<Object, Object> redisTemplate;
	 */

	@Autowired
	private MongoTemplate mongoTemplate;

	public Book save(Book book) {
		return this.repository.save(book);
	}

	public Book findBookByTitle(String title) {
		/*
		 * Book value = (Book) redisTemplate.opsForHash().get("BOOK", title); if (null !=
		 * value) { return value; }
		 */
		Book insertedBook = this.repository.findByTitle(title);
		// redisTemplate.opsForHash().put("BOOK", title, insertedBook);
		return insertedBook;
	}

	public Book updateByTitle(String title, String author) {
		Query query = new Query(Criteria.where("title").is(title));
		Update update = new Update().set("author", author);
		Book result = this.mongoTemplate.findAndModify(query, update,
				new FindAndModifyOptions().returnNew(true).upsert(false), Book.class);
		// redisTemplate.opsForHash().put("BOOK", title, result);
		return result;
	}
}
