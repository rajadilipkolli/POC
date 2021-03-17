package com.example.poc.reactive.service;

import com.example.poc.reactive.entity.ReactivePost;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Flux<ReactivePost> findAllPosts();

    Mono<ReactivePost> savePost(ReactivePost reactivePost);

    Mono<ReactivePost> findPostById(Integer id);

    Mono<ServerResponse> deletePostById(Integer id);

    Mono<ServerResponse> update(Integer id, ReactivePost reactivePost);
}
