/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive;

import com.example.poc.reactive.common.AbstractPostgreSQLContainerBase;
import com.example.poc.reactive.entity.ReactivePost;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ApplicationIntegrationTest extends AbstractPostgreSQLContainerBase {

    @Autowired private WebTestClient webClient;

    @Test
    void willLoadPosts() {
        this.webClient
                .get()
                .uri("/posts")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(ReactivePost.class);
    }
}
