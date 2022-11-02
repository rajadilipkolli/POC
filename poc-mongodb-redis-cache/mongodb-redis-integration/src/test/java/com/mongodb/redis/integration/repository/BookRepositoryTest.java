/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.AbstractMongoContainerBaseTest;
import com.mongodb.redis.integration.document.Book;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class BookRepositoryTest extends AbstractMongoContainerBaseTest {

    @Autowired private BookRepository bookRepository;

    @Autowired private MongoTemplate mongoTemplate;

    private String collectionName;

    private Book bookRecToInsert;

    @BeforeAll
    void setUp() {
        this.collectionName = "books";
        this.bookRecToInsert =
                Book.builder()
                        .title("JUNIT_TITLE")
                        .author("JUNIT_AUTHOR")
                        .bookId("JUNIT")
                        .text("JUNIT_TEXT")
                        .build();
    }

    @AfterAll
    void tearDown() {
        this.mongoTemplate.dropCollection(this.collectionName);
    }

    @Test
    void checkMongoTemplateAndPerFormOperations() {

        assertThat(this.mongoTemplate).isNotNull();
        com.mongodb.client.MongoCollection<Document> createdCollection =
                this.mongoTemplate.createCollection(this.collectionName);
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
