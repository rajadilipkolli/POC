/* Licensed under Apache-2.0 2023 */
package com.example.poc.reactive.controller;

import com.example.poc.reactive.common.AbstractIntegrationTest;
import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

class PostClassicControllerIT extends AbstractIntegrationTest {

    @Autowired private PostRepository reactivePostRepository;

    private Flux<ReactivePost> reactivePostFlux = null;

    @BeforeEach
    void setUp() {
        reactivePostFlux =
                reactivePostRepository
                        .deleteAll()
                        .thenMany(
                                Flux.just(
                                        new ReactivePost("title 1", "content 1"),
                                        new ReactivePost("title 2", "content 2"),
                                        new ReactivePost("title 3", "content 3")))
                        .flatMap(reactivePostRepository::save)
                        .thenMany(reactivePostRepository.findAll());
    }

    @Test
    void all() {
        // Fetch all posts using WebClient
        List<ReactivePost> expectedPosts = reactivePostFlux.collectList().block();

        this.webTestClient
                .mutate() // Mutate the client to add basic authentication headers
                .defaultHeaders(headers -> headers.setBasicAuth("user", "password"))
                .build()
                .get()
                .uri("/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ReactivePost.class)
                .hasSize(expectedPosts.size())
                .isEqualTo(expectedPosts); // Ensure fetched posts match the expected posts
    }

    @Test
    void get() {
        ReactivePost reactivePost = reactivePostFlux.next().block();
        Integer reactivePostId = reactivePost.getId();

        this.webTestClient
                .mutate() // Mutate the client to add basic authentication headers
                .defaultHeaders(
                        headers -> {
                            headers.setBasicAuth("user", "password");
                            headers.setContentType(MediaType.APPLICATION_JSON);
                        })
                .build()
                .get()
                .uri("/posts/{id}", reactivePostId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(reactivePostId)
                .jsonPath("$.title")
                .isEqualTo(reactivePost.getTitle())
                .jsonPath("$.content")
                .isEqualTo(reactivePost.getContent());
    }

    @Test
    void create() {
        PostDto reactivePost = new PostDto("New Title", "New ReactivePost");
        this.webTestClient
                .mutate() // Mutate the client to add basic authentication headers
                .defaultHeaders(
                        headers -> {
                            headers.setBasicAuth("user", "password");
                            headers.setContentType(MediaType.APPLICATION_JSON);
                        })
                .build()
                .post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reactivePost))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .exists("location");
    }

    @Test
    void update() {
        ReactivePost reactivePost = reactivePostFlux.next().block();
        Integer reactivePostId = reactivePost.getId();
        PostDto reactivePostRequest =
                new PostDto("Updated ReactivePost", reactivePost.getContent());

        this.webTestClient
                .mutate() // Mutate the client to add basic authentication headers
                .defaultHeaders(
                        headers -> {
                            headers.setBasicAuth("user", "password");
                            headers.setContentType(MediaType.APPLICATION_JSON);
                        })
                .build()
                .put()
                .uri("/posts/{id}", reactivePostId)
                .body(BodyInserters.fromValue(reactivePostRequest))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(reactivePostId)
                .jsonPath("$.title")
                .isEqualTo("Updated ReactivePost");
    }

    @Test
    void delete() {
        ReactivePost reactivePost = reactivePostFlux.next().block();

        this.webTestClient
                .mutate() // Mutate the client to add basic authentication headers
                .defaultHeaders(headers -> headers.setBasicAuth("admin", "password"))
                .build()
                .delete()
                .uri("/posts/{id}", reactivePost.getId())
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody()
                .isEmpty();
    }
}
