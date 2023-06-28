/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.document.Book;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataMongoTest
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class BookRepositoryTest {

    @Container
    static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo").withTag("6.0.6"));

    static {
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.data.mongodb.host", MONGO_DB_CONTAINER::getHost);
        propertyRegistry.add("spring.data.mongodb.port", MONGO_DB_CONTAINER::getFirstMappedPort);
    }

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
