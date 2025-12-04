/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.service;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.event.PostCreatedEvent;
import com.example.poc.reactive.exception.PostNotFoundException;
import com.example.poc.reactive.mapping.PostMapper;
import com.example.poc.reactive.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;
    private final PostMapper postMapper;

    public PostServiceImpl(
            PostRepository postRepository,
            ApplicationEventPublisher publisher,
            PostMapper postMapper) {
        this.postRepository = postRepository;
        this.publisher = publisher;
        this.postMapper = postMapper;
    }

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
    public Mono<ResponseEntity<Object>> deletePostById(Integer id) {

        return findPostById(id)
                .flatMap(
                        reactivePost -> {
                            log.debug("found post: {}", reactivePost.getId());
                            return this.postRepository
                                    .deleteById(reactivePost.getId())
                                    .then(Mono.just(ResponseEntity.accepted().build()));
                        })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Override
    public Mono<ReactivePost> update(Integer id, PostDto postDto) {
        return this.postRepository
                .findById(id)
                .flatMap(
                        post -> {
                            postMapper.updatePost(postDto, post);
                            return postRepository.save(post);
                        })
                .switchIfEmpty(
                        Mono.error(new PostNotFoundException("Post with Id " + id + " NotFound!")));
    }
}
