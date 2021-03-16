package com.example.poc.reactive.service;

import com.example.poc.reactive.common.AbstractPostgreSQLContainerBase;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.repository.PostRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({PostServiceImpl.class})
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

        Flux<ReactivePost> composite = this.postService.findAllPosts().thenMany(saved);

        StepVerifier.create(composite).expectNextCount(3).verifyComplete();
    }

    @Test
    void save() {
        Mono<ReactivePost> reactivePostMono =
                this.postService.savePost(
                        ReactivePost.builder().title("Raja").content("fourth Content").build());

        StepVerifier.create(reactivePostMono)
                .expectNextMatches(
                        saved ->
                                StringUtils.hasText(String.valueOf(saved.getId()))
                                        && saved.getTitle().equalsIgnoreCase("Raja")
                                        && saved.getContent().equalsIgnoreCase("fourth Content"))
                .verifyComplete();
    }

    @Test
    void delete() {

        ReactivePost test = ReactivePost.builder().title("Hello").content("fifth Content").build();
        Mono<ReactivePost> deleted =
                this.postService
                        .savePost(test)
                        .flatMap(saved -> this.postService.deletePostById(saved.getId()));
        StepVerifier.create(deleted)
                .expectNextMatches(
                        reactivePost ->
                                reactivePost.getContent().equalsIgnoreCase(test.getContent())
                                        && reactivePost
                                                .getTitle()
                                                .equalsIgnoreCase(test.getTitle()))
                .verifyComplete();
    }

    @Test
    void update() {
        ReactivePost test = ReactivePost.builder().title("Junit").content("sixth Content").build();
        ReactivePost test1 =
                ReactivePost.builder().title("Junit1").content("sixth Content").build();

        Mono<ReactivePost> saved =
                this.postService
                        .savePost(test)
                        .flatMap(p -> this.postService.update(p.getId(), test1));

        StepVerifier.create(saved)
                .expectNextMatches(
                        reactivePost -> reactivePost.getTitle().equalsIgnoreCase("Junit1"))
                .verifyComplete();
    }

    @Test
    void getById() {
        ReactivePost test =
                ReactivePost.builder().title("Junit_new").content("seventh Content").build();
        Mono<ReactivePost> deleted =
                this.postService
                        .savePost(test)
                        .flatMap(saved -> this.postService.findPostById(saved.getId()));
        StepVerifier.create(deleted)
                .expectNextMatches(
                        reactivePost ->
                                StringUtils.hasText(String.valueOf(reactivePost.getId()))
                                        && test.getTitle()
                                                .equalsIgnoreCase(reactivePost.getTitle()))
                .verifyComplete();
    }
}
