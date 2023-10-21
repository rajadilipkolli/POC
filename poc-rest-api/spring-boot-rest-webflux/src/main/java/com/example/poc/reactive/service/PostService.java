/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.service;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {

    Flux<ReactivePost> findAllPosts();

    Mono<ReactivePost> savePost(PostDto postDto);

    Mono<ReactivePost> findPostById(Integer id);

    Mono<ResponseEntity<Object>> deletePostById(Integer id);

    Mono<ReactivePost> update(Integer id, PostDto postDto);
}
