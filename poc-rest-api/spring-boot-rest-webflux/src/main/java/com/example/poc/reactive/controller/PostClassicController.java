/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.controller;

import com.example.poc.reactive.dto.PostDto;
import com.example.poc.reactive.entity.ReactivePost;
import com.example.poc.reactive.service.PostService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
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

    @GetMapping
    public Publisher<ReactivePost> all() {
        return this.postService.findAllPosts();
    }

    @GetMapping("/{id}")
    public Mono<ReactivePost> get(@PathVariable("id") Integer id) {
        return this.postService.findPostById(id);
    }

    @PostMapping
    public Publisher<ResponseEntity<ReactivePost>> create(@RequestBody PostDto postDto) {
        return this.postService
                .savePost(postDto)
                .map(
                        persistedPost ->
                                ResponseEntity.created(
                                                URI.create("/posts/" + persistedPost.getId()))
                                        .build());
    }

    @PutMapping("/{id}")
    public Mono<ServerResponse> update(
            @PathVariable("id") Integer id, @RequestBody PostDto postDto) {
        return this.postService.update(id, postDto);
    }

    @DeleteMapping("/{id}")
    public Mono<ServerResponse> delete(@PathVariable("id") Integer id) {
        return this.postService.deletePostById(id);
    }
}
