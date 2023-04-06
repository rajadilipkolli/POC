/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.service;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.event.PostCreatedEvent;
import com.example.poc.reactive.exception.PostNotFoundException;
import com.example.poc.reactive.mapping.PostMapper;
import com.example.poc.reactive.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;
    private final PostMapper postMapper;

    @Override
    public Flux<ReactivePost> findAllPosts() {
        return this.postRepository.findAll();
    }

    @Override
    public Mono<ReactivePost> savePost(PostDto postDto) {
        ReactivePost reactivePost = this.postMapper.toEntity(postDto);
        return this.postRepository
                .save(reactivePost)
                .doOnSuccess(post -> this.publisher.publishEvent(new PostCreatedEvent(post)));
    }

    @Override
    public Mono<ReactivePost> findPostById(Integer id) {
        return this.postRepository
                .findById(id)
                .switchIfEmpty(
                        Mono.error(new PostNotFoundException("Post with Id " + id + " NotFound!")));
    }

    @Override
    public Mono<ServerResponse> deletePostById(Integer id) {
        return this.postRepository
                .findById(id)
                .flatMap(
                        p -> {
                            log.debug("found post: {}", p);
                            return this.postRepository.deleteById(p.getId()).thenReturn(p);
                        })
                .flatMap(deleted -> accepted().build())
                .switchIfEmpty(notFound().build());
    }

    @Override
    public Mono<ServerResponse> update(Integer id, ReactivePost reactivePost) {
        return this.postRepository
                .findById(id)
                .map(
                        post -> {
                            post.setTitle(reactivePost.getTitle());
                            post.setContent(reactivePost.getContent());
                            return post;
                        })
                .flatMap(this.postRepository::save)
                .flatMap(post -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}
