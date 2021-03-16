package com.example.poc.reactive.service;

import com.example.poc.reactive.entity.ReactivePost;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Flux<ReactivePost> findAllPosts();

    Mono<ReactivePost> savePost(ReactivePost reactivePost);

    Mono<ReactivePost> findPostById(Integer id);

    Mono<ReactivePost> deletePostById(Integer id);

    Mono<ReactivePost> update(Integer id, ReactivePost reactivePost);
}
