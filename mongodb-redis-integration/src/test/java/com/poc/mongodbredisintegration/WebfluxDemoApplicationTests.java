package com.poc.mongodbredisintegration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.repository.BookReactiveRepository;

import reactor.core.publisher.Mono;

public class WebfluxDemoApplicationTests extends AbstractMongoDBRedisIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    BookReactiveRepository bookReactiveRepository;

    @Test
    public void testCreateBook() {
        Book book = Book.builder().author("Raja").text("This is a Test Book")
                .title("JUNIT_TITLE").build();

        webTestClient.post().uri("/Books").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(book), Book.class)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBody()
                .jsonPath("$.id").isNotEmpty().jsonPath("$.text")
                .isEqualTo("This is a Test Book");
    }

    @Test
    public void testGetAllBooks() {
        webTestClient.get().uri("/Books").accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Book.class);
    }

    @Test
    public void testGetSingleBook() {
        Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        webTestClient.get()
                .uri("/Books/{id}", Collections.singletonMap("id", book.getId()))
                .exchange().expectStatus().isOk().expectBody().consumeWith(
                        response -> assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateBook() {
        Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        Book newBookData = Book.builder().author("Raja").text("Updated Book")
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
        Book book = bookReactiveRepository.save(Book.builder().author("Raja")
                .text("This is a Test Book").title("JUNIT_TITLE").build()).block();

        webTestClient.delete()
                .uri("/Books/{id}", Collections.singletonMap("id", book.getId()))
                .exchange().expectStatus().isOk();
    }

}
