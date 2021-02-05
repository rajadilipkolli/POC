package com.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongoDBRedisApplicationTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private WebTestClient webTestClient;

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
}
