/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.TestContainersConfig;
import com.mongodb.redis.integration.document.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@Import(TestContainersConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveBookRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ReactiveBookRepositoryTest.class);
    @Autowired ReactiveBookRepository bookRepository;

    @BeforeAll
    void setUp() {
        Book book = new Book().setTitle("prius").setAuthor("hybrid").setText("Junit");
        this.bookRepository
                .deleteAll()
                .thenMany(Flux.just(book))
                .flatMap(bookRepository::save)
                .doOnNext(insertedBook -> log.info("Inserted Book is : {}", insertedBook))
                .blockLast();
    }

    @Test
    void getAllBooks() {
        Flux<Book> allBooks = bookRepository.findAll();

        StepVerifier.create(allBooks.log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByTitleReturnsBook() {
        StepVerifier.create(this.bookRepository.findByTitle("prius"))
                .consumeNextWith(
                        (Book book) -> {
                            assertThat(book.getTitle()).isEqualTo("prius");
                            assertThat(book.getAuthor()).isEqualTo("hybrid");
                            assertThat(book.getText()).isEqualTo("Junit");
                            assertThat(book.getBookId()).isNotNull();
                            assertThat(book.getVersion()).isZero();
                        })
                .verifyComplete();
    }
}
