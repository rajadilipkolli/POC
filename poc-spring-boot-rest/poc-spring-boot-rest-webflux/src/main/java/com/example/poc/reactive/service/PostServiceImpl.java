package com.example.poc.reactive.service;

import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.event.PostCreatedEvent;
import com.example.poc.reactive.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public Flux<ReactivePost> findAllPosts() {
        return this.postRepository.findAll();
    }

    @Override
    public Mono<ReactivePost> savePost(ReactivePost reactivePost) {
        return this.postRepository
                .save(reactivePost)
                .doOnSuccess(post -> this.publisher.publishEvent(new PostCreatedEvent(post)));
    }

    @Override
    public Mono<ReactivePost> findPostById(Integer id) {
        return this.postRepository.findById(id);
    }

    @Override
    public Mono<ReactivePost> deletePostById(Integer id) {
        return this.postRepository
                .findById(id)
                .flatMap(p -> this.postRepository.deleteById(p.getId()).thenReturn(p));
    }

    @Override
    public Mono<ReactivePost> update(Integer id, ReactivePost reactivePost) {
        return this.postRepository
                .findById(id)
                .map(
                        post -> {
                            post.setTitle(reactivePost.getTitle());
                            post.setContent(reactivePost.getContent());
                            return post;
                        })
                .flatMap(this.postRepository::save);
    }
}
