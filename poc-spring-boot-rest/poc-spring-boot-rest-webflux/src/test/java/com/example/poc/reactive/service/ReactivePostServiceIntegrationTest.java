package com.example.poc.reactive.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.reactive.common.AbstractPostgreSQLContainerBase;
import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
@Slf4j
class ReactivePostServiceIntegrationTest extends AbstractPostgreSQLContainerBase {

    @Autowired private PostRepository postRepository;

    @Autowired private PostService postService;

    @AfterAll
    void tearDown() {
        Mono<Void> deleted = this.postRepository.deleteByIdNot(0);
        StepVerifier.create(deleted).verifyComplete();
    }

    @Test
    void getAll() {
        Flux<ReactivePost> saved =
                this.postRepository.saveAll(
                        Flux.just(
                                ReactivePost.builder()
                                        .title("Josh")
                                        .content("third Content")
                                        .build(),
                                ReactivePost.builder()
                                        .title("Matt")
                                        .content("first Content")
                                        .build(),
                                ReactivePost.builder()
                                        .title("Jane")
                                        .content("second Content")
                                        .build()));

        var composite = this.postService.findAllPosts().thenMany(saved);

        StepVerifier.create(composite).expectNextCount(3).verifyComplete();
    }

    @Test
    void save() {
        Mono<ReactivePost> reactivePostMono =
                this.postService.savePost(new PostDto("Raja", "fourth Content"));

        StepVerifier.create(reactivePostMono)
                .consumeNextWith(
                        reactivePost -> {
                            log.info("saved post: {}", reactivePost);
                            assertThat(reactivePost.getTitle()).isEqualTo("Raja");
                            assertThat(reactivePost.getContent()).isEqualTo("fourth Content");
                            assertThat(reactivePost.getCreatedAt()).isNotNull();
                            assertThat(reactivePost.getUpdatedAt()).isNotNull();
                            assertThat(reactivePost.getCreatedBy()).isEqualTo("user");
                            assertThat(reactivePost.getUpdatedBy()).isEqualTo("user");
                        })
                .verifyComplete();
    }

    @Test
    void delete() {

        Mono<ServerResponse> deleted =
                this.postService
                        .savePost(new PostDto("Hello", "fifth Content"))
                        .flatMap(
                                saved -> {
                                    log.info("saved post: {}", saved);
                                    return this.postService.deletePostById(saved.getId());
                                });
        StepVerifier.create(deleted)
                .consumeNextWith(
                        reactivePost ->
                                assertThat(reactivePost.statusCode())
                                        .isEqualTo(HttpStatus.ACCEPTED))
                .verifyComplete();
    }

    @Test
    void update() {
        ReactivePost test1 =
                ReactivePost.builder().title("Junit1").content("sixth Content").build();

        Mono<ServerResponse> saved =
                this.postService
                        .savePost(new PostDto("Junit1", "sixth Content"))
                        .flatMap(p -> this.postService.update(p.getId(), test1));

        StepVerifier.create(saved)
                .expectNextMatches(
                        reactivePost -> reactivePost.statusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();
    }

    @Test
    void getById() {
        ReactivePost test =
                ReactivePost.builder().title("Junit_new").content("seventh Content").build();
        Mono<ReactivePost> responseMono =
                this.postService
                        .savePost(new PostDto("Junit_new", "seventh Content"))
                        .flatMap(
                                reactivePost ->
                                        this.postService.findPostById(reactivePost.getId()));
        StepVerifier.create(responseMono)
                .expectNextMatches(
                        reactivePost ->
                                StringUtils.hasText(String.valueOf(reactivePost.getId()))
                                        && test.getTitle()
                                                .equalsIgnoreCase(reactivePost.getTitle()))
                .verifyComplete();
    }
}
