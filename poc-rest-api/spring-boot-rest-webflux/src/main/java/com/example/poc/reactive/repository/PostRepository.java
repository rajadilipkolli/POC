/* Licensed under Apache-2.0 2023 */
package com.example.poc.reactive.repository;

import com.example.poc.reactive.dto.PostSummary;
import com.example.poc.reactive.entity.ReactivePost;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PostRepository extends R2dbcRepository<ReactivePost, Integer> {

    @Query("select * from reactive_posts p where p.content = :content")
    Flux<ReactivePost> findByContent(String content);

    Mono<Void> deleteByIdNot(Integer integer);

    Flux<PostSummary> findByTitleContains(String title);
}
