package com.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.AbstractRedisTestContainer;
import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.repository.BookReactiveRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestPropertySource(
    properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class MongoDBRedisReactiveApplicationIntegrationTest extends AbstractRedisTestContainer {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private WebTestClient webTestClient;

  @Autowired private ReactiveMongoOperations operations;

  @Autowired private BookReactiveRepository bookReactiveRepository;

  @BeforeAll
  void init() {

    this.operations
        .collectionExists(Book.class)
        .subscribe(
            aBoolean -> {
              if (!aBoolean) {
                this.operations
                    .createCollection(
                        Book.class, CollectionOptions.empty().size(1024 * 1024).maxDocuments(100))
                    .subscribe();
              }
            });

    this.bookReactiveRepository
        .save(
            Book.builder()
                .title("MongoDbCookBook")
                .text("MongoDB Data Book")
                .author("Raja")
                .bookId("1")
                .build())
        .subscribe();
  }

  @Test
  @DisplayName("Traditional way")
  void should_return_hello_world() {
    ResponseEntity<String> response = this.testRestTemplate.getForEntity("/", String.class);

    // assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotBlank().isEqualTo("Hello World!");
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
    EntityExchangeResult<List<Book>> result =
        this.webTestClient
            .get()
            .uri("/api/book/")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Book.class)
            .returnResult();
    assertThat(result.getResponseBody()).size().isGreaterThanOrEqualTo(1);
    assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
    assertThat(result.getUriTemplate()).isEqualTo("/api/book/");
    Book response = Objects.requireNonNull(result.getResponseBody()).get(0);
    assertThat(response.getTitle()).isEqualTo("MongoDbCookBook");
    assertThat(response.getAuthor()).isEqualTo("Raja");
    assertThat(response.getText()).isEqualTo("MongoDB Data Book");
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
        .expectBody(Book.class)
        .consumeWith(
            (response) -> {
              assertThat(response.getResponseBody()).isNotNull();
              assertThat(response.getResponseBody().getTitle()).isEqualTo("MongoDbCookBook");
              assertThat(response.getResponseBody().getAuthor()).isEqualTo("Raja");
              assertThat(response.getResponseBody().getText()).isEqualTo("MongoDB Data Book");
            });
  }

  @Test
  void test03PostBook() {
    final Book book =
        Book.builder().author("Raja").text("This is a Test Book").title("JUNIT_TITLE").build();

    this.webTestClient
        .post()
        .uri("/api/book/")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(book))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectHeader()
        .exists("location")
        .expectBody(Book.class)
        .consumeWith((response) -> assertThat(response.getResponseBody()).isNull());
  }

  @Test
  void test04PutBook() {
    Book book =
        Book.builder()
            .title("MongoDbCookBook")
            .text("MongoDB Data Book1")
            .author("Raja")
            .bookId("1")
            .build();

    this.webTestClient
        .put()
        .uri("/api/book/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(book))
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
        .uri("/api/book/{id}", 1)
        .exchange()
        .expectStatus()
        .isAccepted()
        .expectBody(Book.class);
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
        Book.builder()
            .author("Raja")
            .text("This is a Test Book")
            .title(RandomStringUtils.randomAlphanumeric(200))
            .build();

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
        .contentType("application/problem+json")
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("400")
        .jsonPath("$.title")
        .isEqualTo("Bad Request")
        .jsonPath("$.detail")
        .isNotEmpty();

    book = Book.builder().build();
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
        .contentType("application/problem+json")
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo("400")
        .jsonPath("$.title")
        .isEqualTo("Bad Request")
        .jsonPath("$.detail")
        .isNotEmpty();
  }
}
