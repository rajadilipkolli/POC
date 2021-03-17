package com.example.poc.reactive.controller;

import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.service.PostService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostClassicController {

    private final PostService postService;

    private final MediaType mediaType = MediaType.APPLICATION_JSON;

    @GetMapping
    public Publisher<ReactivePost> all() {
        return this.postService.findAllPosts();
    }

    @GetMapping("/{id}")
    public Mono<ReactivePost> get(@PathVariable("id") Integer id) {
        return this.postService.findPostById(id);
    }

    @PostMapping
    public Publisher<ResponseEntity<ReactivePost>> create(@RequestBody ReactivePost reactivePost) {
        return this.postService
                .savePost(reactivePost)
                .map(
                        persistedPost ->
                                ResponseEntity.created(
                                                URI.create("/posts/" + persistedPost.getId()))
                                        .contentType(this.mediaType)
                                        .build());
    }

    @PutMapping("/{id}")
    public Mono<ServerResponse> update(
            @PathVariable("id") Integer id, @RequestBody ReactivePost reactivePost) {
        return this.postService.update(id, reactivePost);
    }

    @DeleteMapping("/{id}")
    public Mono<ServerResponse> delete(@PathVariable("id") Integer id) {
        return this.postService.deletePostById(id);
    }
}
