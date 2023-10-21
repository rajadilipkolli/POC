/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive;

import com.example.poc.reactive.common.AbstractIntegrationTest;
import com.example.poc.reactive.entity.ReactivePost;
import org.junit.jupiter.api.Test;

class ApplicationIntegrationTest extends AbstractIntegrationTest {

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
