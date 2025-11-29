/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.AbstractIntegrationTest;
import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.request.BookDTO;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

class MongoDBRedisReactiveApplicationIntegrationTest extends AbstractIntegrationTest {

    @BeforeAll
    void init() {

        this.operations
                .collectionExists(Book.class)
                .map(
                        aBoolean -> {
                            if (!aBoolean) {
                                {
                                    return this.operations
                                            .createCollection(
                                                    Book.class,
                                                    CollectionOptions.empty()
                                                            .size(1024 * 1024)
                                                            .maxDocuments(100))
                                            .subscribe();
                                }
                            }
                            return Mono.empty();
                        })
                .then(
                        this.reactiveBookRepository.save(
                                new Book()
                                        .setTitle("MongoDbCookBook")
                                        .setText("MongoDB Data Book")
                                        .setAuthor("Raja")
                                        .setBookId("1")))
                .subscribe();
    }

    @Test
    @DisplayName("Traditional way")
    void should_return_hello_world() {
        this.webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(
                        stringEntityExchangeResult -> {
                            assertThat(stringEntityExchangeResult.getResponseBody())
                                    .isNotBlank()
                                    .isEqualTo("Hello World!");
                        });
    }

    @Test
    @DisplayName("WebFlux way")
    void should_return_hello_world_web_flux() {
        String response =
                webTestClient
                        .get()
                        .uri("/")
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(String.class)
                        .returnResult()
                        .getResponseBody();

        // assert
        assertThat(response).isNotBlank().isEqualTo("Hello World!");
    }

    @Test
    void test01GetAll() {
        EntityExchangeResult<List<BookDTO>> result =
                this.webTestClient
                        .get()
                        .uri("/api/book/")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(BookDTO.class)
                        .returnResult();
        assertThat(result.getResponseBody()).size().isGreaterThanOrEqualTo(1);
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
        assertThat(result.getUriTemplate()).isEqualTo("/api/book/");
        BookDTO response = Objects.requireNonNull(result.getResponseBody()).getFirst();
        assertThat(response.title()).isEqualTo("MongoDbCookBook");
        assertThat(response.author()).isEqualTo("Raja");
        assertThat(response.text()).isEqualTo("MongoDB Data Book");
    }

    @Test
    void test02GetBook() {
        this.webTestClient
                .get()
                .uri("/api/book/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDTO.class)
                .consumeWith(
                        (response) -> {
                            assertThat(response.getResponseBody()).isNotNull();
                            assertThat(response.getResponseBody().title())
                                    .isEqualTo("MongoDbCookBook");
                            assertThat(response.getResponseBody().author()).isEqualTo("Raja");
                            assertThat(response.getResponseBody().text())
                                    .isEqualTo("MongoDB Data Book");
                        });
    }

    @Test
    void test03PostBook() {
        BookDTO bookDTO = new BookDTO(null, "JUNIT_TITLE", "Raja", "This is a Test Book", 0L);

        this.webTestClient
                .post()
                .uri("/api/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDTO))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectHeader()
                .exists("location")
                .expectBody(BookDTO.class)
                .consumeWith((response) -> assertThat(response.getResponseBody()).isNull());
    }

    @Test
    void test04PutBook() {
        BookDTO bookDTO = new BookDTO("1", "MongoDbCookBook", "Raja", "MongoDB Data Book1", 0L);

        this.webTestClient
                .put()
                .uri("/api/book/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bookDTO))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].text")
                .isEqualTo("MongoDB Data Book1");
    }

    @Test
    void test05DeleteBook() {
        this.webTestClient
                .delete()
                .uri("/api/book/{id}", Collections.singletonMap("id", 1))
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody()
                .isEmpty();
        this.webTestClient
                .get()
                .uri("/api/book/{id}", Collections.singletonMap("id", 1))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void test06DeleteBook() {
        this.webTestClient
                .delete()
                .uri("/api/book/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted();
    }

    @Test
    @DisplayName("Invalid Data")
    void testCreateBookFail() {
        Book book =
                new Book()
                        .setAuthor("Raja")
                        .setText("This is a Test Book")
                        .setTitle(RandomStringUtils.secure().nextAlphabetic(200));

        this.webTestClient
                .post()
                .uri("/api/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("400");

        book = new Book();
        this.webTestClient
                .post()
                .uri("/api/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(book))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectHeader()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .jsonPath("$.status")
                .isEqualTo("400");
    }
}
