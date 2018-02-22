/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;

import reactor.core.publisher.Mono;

@TestInstance(Lifecycle.PER_CLASS)
public class WebfluxDemoApplicationTests extends AbstractMongoDBRedisIntegrationTest {

    private static final String TITLE = "JUNIT_TITLE";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    BookReactiveRepository bookReactiveRepository;

    @BeforeAll
    public void setUp() {
        final List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final Book book = new Book();
            book.setTitle(TITLE + String.valueOf(i));
            bookList.add(book);
        }
        bookReactiveRepository.saveAll(bookList);
    }

    @Test
    public void testCreateBook() {
        final Book book = Book.builder().author("Raja").text("This is a Test Book")
                .title("JUNIT_TITLE").build();

        webTestClient.post().uri("/Books").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(book), Book.class)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
                .jsonPath("$.id").isNotEmpty().jsonPath("$.text")
                .isEqualTo("This is a Test Book");
    }
    
    @Test
    @DisplayName("Invalid Data")
    public void testCreateBookFail() {
        final Book book = Book.builder().author("Raja").text("This is a Test Book")
                .title(RandomStringUtils.randomAlphanumeric(200)).build();

        webTestClient.post().uri("/Books").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(book), Book.class)
                .exchange().expectStatus().isBadRequest().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
                .jsonPath("$.message").isEqualTo("Validation failed for object='book'. Error count: 1");
    }

    @Test
    public void testGetAllBooks() {
        webTestClient.get().uri("/Books").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Book.class);
    }

    @Test
    public void testGetSingleBook() {
        final Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        webTestClient.get()
                .uri("/Books/{id}", Collections.singletonMap("id", book.getId()))
                .exchange().expectStatus().isOk().expectBody().consumeWith(
                        response -> assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateBook() {
        final Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        final Book newBookData = Book.builder().author("Raja").text("Updated Book")
                .title("JUNIT_TITLE").build();

        webTestClient.put()
                .uri("/Books/{id}", Collections.singletonMap("id", book.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newBookData), Book.class).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
                .jsonPath("$.text").isEqualTo("Updated Book");
    }

    @Test
    public void testDeleteBook() {
        final Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        webTestClient.delete()
                .uri("/Books/{id}", Collections.singletonMap("id", book.getId()))
                .exchange().expectStatus().isOk();
    }

}
