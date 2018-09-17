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

package org.mongodb.redis.integration.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runners.MethodSorters;
import org.mongodb.redis.integration.document.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
class BookRepositoryTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	private String collectionName;

	private Book bookRecToInsert;

	@BeforeAll
	void before() {
		this.collectionName = "books";
		this.bookRecToInsert = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR")
				.bookId("JUNIT").text("JUNIT_TEXT").build();
	}

	@AfterAll
	void after() {
		this.mongoTemplate.dropCollection(this.collectionName);
	}

	@Test
	void checkMongoTemplateAndPerFormOperations() {

		assertThat(this.mongoTemplate).isNotNull();
		MongoCollection<Document> createdCollection = this.mongoTemplate
				.createCollection(this.collectionName);
		assertThat(createdCollection.countDocuments()).isEqualTo(0);
		assertThat(this.mongoTemplate.collectionExists(this.collectionName)).isTrue();

		assertThat(this.bookRepository).isNotNull();
		Book savedLogRecord = this.bookRepository.save(this.bookRecToInsert);
		assertThat(this.bookRepository.findById(savedLogRecord.getBookId())).isNotNull();
		Book foundBook = this.bookRepository.findBookByTitle("JUNIT_TITLE").get();
		assertThat(foundBook).isNotNull();
		assertThat(foundBook.getTitle()).isEqualTo("JUNIT_TITLE");

	}

}
