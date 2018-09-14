package org.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

import com.mongodb.client.MongoCollection;

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
	public void before() {
		collectionName = "books";
		bookRecToInsert = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR")
				.bookId("JUNIT").text("JUNIT_TEXT").build();
	}

	@AfterAll
	public void after() {
		mongoTemplate.dropCollection(collectionName);
	}

	@Test
	public void checkMongoTemplateAndPerFormOperations() {

		assertThat(mongoTemplate).isNotNull();
		MongoCollection<Document> createdCollection = mongoTemplate
				.createCollection(collectionName);
		assertThat(createdCollection.countDocuments()).isEqualTo(0);
		assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();

		assertThat(bookRepository).isNotNull();
		Book savedLogRecord = bookRepository.save(bookRecToInsert);
		assertThat(bookRepository.findById(savedLogRecord.getBookId())).isNotNull();
		Book foundBook = bookRepository.findBookByTitle("JUNIT_TITLE");
		assertThat(foundBook).isNotNull();
		assertThat(foundBook.getTitle()).isEqualTo("JUNIT_TITLE");

	}

}
