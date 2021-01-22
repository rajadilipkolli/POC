/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.reactivepoc.service;

import com.poc.reactivepoc.entity.ReactivePost;
import com.poc.reactivepoc.event.PostCreatedEvent;
import com.poc.reactivepoc.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

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
		return this.postRepository.save(reactivePost)
				.doOnSuccess(post -> this.publisher.publishEvent(new PostCreatedEvent(post)));
	}

	@Override
	public Mono<ReactivePost> findPostById(Integer id) {
		return this.postRepository.findById(id);
	}

	@Override
	public Mono<ReactivePost> deletePostById(Integer id) {
		return this.postRepository.findById(id).flatMap(p -> this.postRepository.deleteById(p.getId()).thenReturn(p));
	}

	@Override
	public Mono<ReactivePost> update(Integer id, ReactivePost reactivePost) {
		return this.postRepository.findById(id).map(post -> {
			post.setTitle(reactivePost.getTitle());
			post.setContent(reactivePost.getContent());
			return post;
		}).flatMap(this.postRepository::save);
	}

}
