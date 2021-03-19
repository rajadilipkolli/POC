package com.example.poc.reactive;

import com.example.poc.reactive.common.AbstractPostgreSQLContainerBase;
import com.example.poc.reactive.entity.ReactivePost;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest extends AbstractPostgreSQLContainerBase {

    @LocalServerPort private int port;

    private WebTestClient webClient;

    @BeforeAll
    void setup() {
        this.webClient =
                WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();
    }

    @Test
    public void willLoadPosts() {
        this.webClient
                .get()
                .uri("/posts")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(ReactivePost.class);
    }
}
