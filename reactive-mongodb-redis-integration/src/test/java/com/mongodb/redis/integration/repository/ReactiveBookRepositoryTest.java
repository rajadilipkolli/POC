package com.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.MongoDBTestContainer;
import com.mongodb.redis.integration.document.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
class ReactiveBookRepositoryTest extends MongoDBTestContainer {

  @Autowired ReactiveBookRepository bookRepository;

  @BeforeAll
  void setUp() {
    this.bookRepository.save(Book.builder().title("prius").author("hybrid").build()).then().block();
  }

  @AfterAll
  void tearDown() {
    this.bookRepository.deleteAll().then().block();
  }

  @Test
  void findByTitleReturnsBook() {
    StepVerifier.create(this.bookRepository.findByTitle("prius"))
        .consumeNextWith(
            (Book book) -> {
              assertThat(book.getTitle()).isEqualTo("prius");
              assertThat(book.getAuthor()).isEqualTo("hybrid");
            })
        .verifyComplete();
  }
}
