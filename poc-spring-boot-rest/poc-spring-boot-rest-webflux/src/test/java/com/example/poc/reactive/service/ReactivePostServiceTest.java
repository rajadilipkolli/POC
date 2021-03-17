package com.example.poc.reactive.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.reactive.common.AbstractPostgreSQLContainerBase;
import com.example.poc.reactive.config.DatabaseConfig;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({PostServiceImpl.class, DatabaseConfig.class})
@WithMockUser
@Slf4j
class ReactivePostServiceTest extends AbstractPostgreSQLContainerBase {

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
                this.postService.savePost(
                        ReactivePost.builder().title("Raja").content("fourth Content").build());

        StepVerifier.create(reactivePostMono)
                .consumeNextWith(
                        p -> {
                            log.info("saved post: {}", p);
                            assertThat(p.getTitle()).isEqualTo("Raja");
                            assertThat(p.getContent()).isEqualTo("fourth Content");
                            assertThat(p.getCreatedAt()).isNotNull();
                            assertThat(p.getUpdatedAt()).isNotNull();
                            assertThat(p.getCreatedBy()).isEqualTo("user");
                            assertThat(p.getUpdatedBy()).isEqualTo("user");
                        })
                .verifyComplete();
    }

    @Test
    void delete() {

        ReactivePost test = ReactivePost.builder().title("Hello").content("fifth Content").build();
        Mono<ServerResponse> deleted =
                this.postService
                        .savePost(test)
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
        ReactivePost test = ReactivePost.builder().title("Junit").content("sixth Content").build();
        ReactivePost test1 =
                ReactivePost.builder().title("Junit1").content("sixth Content").build();

        Mono<ServerResponse> saved =
                this.postService
                        .savePost(test)
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
                        .savePost(test)
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
