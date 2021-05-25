package com.mongodb.redis.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.document.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DataMongoTest
@Slf4j
class BookReactiveRepositoryTest {

    @Autowired BookReactiveRepository bookRepository;

    @BeforeAll
    void setUp() {
        Book book = Book.builder().title("prius").author("hybrid").text("Junit").build();
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
                            assertThat(book.getVersion()).isEqualTo(0);
                        })
                .verifyComplete();
    }
}
