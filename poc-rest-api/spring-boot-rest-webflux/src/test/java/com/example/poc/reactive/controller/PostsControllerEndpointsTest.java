/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.service.PostService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = {PostClassicController.class})
@AutoConfigureWebTestClient
@WithMockUser(username = "username")
class PostsControllerEndpointsTest {

    @MockBean private PostService postService;

    @Autowired private WebTestClient webTestClient;

    @Test
    void getAll() {

        given(this.postService.findAllPosts())
                .willReturn(
                        Flux.just(
                                ReactivePost.builder().id(1).content("A").build(),
                                ReactivePost.builder().id(2).content("B").build()));

        this.webTestClient
                .get()
                .uri("/posts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id")
                .isEqualTo("1")
                .jsonPath("$.[0].content")
                .isEqualTo("A")
                .jsonPath("$.[1].id")
                .isEqualTo("2")
                .jsonPath("$.[1].content")
                .isEqualTo("B");
    }

    @Test
    void save() {
        String content = UUID.randomUUID().toString();
        ReactivePost data = ReactivePost.builder().id(123).content(content).build();
        PostDto postDto = new PostDto("title", content);
        given(this.postService.savePost(any(PostDto.class))).willReturn(Mono.just(data));
        this.webTestClient
                .mutateWith(csrf())
                .post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(postDto), PostDto.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void delete() {
        ReactivePost data =
                ReactivePost.builder().id(123).content(UUID.randomUUID().toString()).build();
        given(this.postService.deletePostById(data.getId()))
                .willReturn(ServerResponse.accepted().build());
        this.webTestClient
                .mutateWith(csrf())
                .delete()
                .uri("/posts/" + data.getId())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void update() {
        PostDto data = new PostDto("title", "content");

        given(this.postService.update(123, data)).willReturn(ServerResponse.noContent().build());

        this.webTestClient
                .mutateWith(csrf())
                .put()
                .uri("/posts/123")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), ReactivePost.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getById() {

        ReactivePost data = ReactivePost.builder().id(1).content("A").build();

        given(this.postService.findPostById(data.getId())).willReturn(Mono.just(data));

        this.webTestClient
                .get()
                .uri("/posts/" + data.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(data.getId())
                .jsonPath("$.content")
                .isEqualTo(data.getContent());
    }
}
