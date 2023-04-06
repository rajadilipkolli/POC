/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.AbstractRedisContainerBaseTest;
import com.mongodb.redis.integration.document.Book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MongoDBRedisApplicationIntegrationTest extends AbstractRedisContainerBaseTest {

    @Autowired private TestRestTemplate testRestTemplate;

    @Test
    void test_application_running() {
        // act
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/", String.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isEqualTo("Hello World!");
    }

    @Test
    void test_both_containers_are_running() {
        assertThat(MONGO_DB_CONTAINER.isRunning()).isTrue();
        assertThat(REDIS_DB_CONTAINER.isRunning()).isTrue();
    }

    @Test
    void should_return_hello_world() {
        ResponseEntity<String> response = this.testRestTemplate.getForEntity("/", String.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank().isEqualTo("Hello World!");
    }

    @Test
    void getBookByTitle_returnsBookDetails() {
        // arrange
        Book book =
                Book.builder()
                        .title("MongoDbCookBook")
                        .author("Raja")
                        .bookId("book1")
                        .text("text1")
                        .build();
        ResponseEntity<Book> response =
                this.testRestTemplate.postForEntity("/book/saveBook", book, Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");

        // act
        response =
                this.testRestTemplate.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
        assertThat(response.getBody().getAuthor()).isEqualTo("Raja");

        // act by Update
        response =
                this.testRestTemplate.exchange(
                        "/book/updateByTitle/MongoDbCookBook/Raja1",
                        HttpMethod.PUT,
                        null,
                        Book.class);

        // assert After Update
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
        assertThat(response.getBody().getAuthor()).isEqualTo("Raja1");

        // act by Delete
        ResponseEntity<String> resp =
                this.testRestTemplate.exchange(
                        "/book/deleteByTitle/MongoDbCookBook",
                        HttpMethod.DELETE,
                        null,
                        String.class);
        assertThat(resp).isNotNull();
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(resp.getBody()).isEqualTo("Book with title MongoDbCookBook deleted");

        response =
                this.testRestTemplate.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        resp = this.testRestTemplate.getForEntity("/book/deleteCache", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo("Deleted Full Cache");
    }
}
