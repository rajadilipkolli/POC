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

import com.poc.reactivepoc.dataload.DataInitializer;
import com.poc.reactivepoc.entity.ReactivePost;
import com.poc.reactivepoc.repository.PostRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

@DataR2dbcTest
@Import({ PostServiceImpl.class, DataInitializer.class })
class ReactivePostServiceTest {

	@Autowired
	private PostRepository repository;

	@Autowired
	private PostService service;

	@Test
	void getAll() {
		Flux<ReactivePost> saved = this.repository
				.saveAll(Flux.just(ReactivePost.builder().title("Josh").content("third Content").build(),
						ReactivePost.builder().title("Matt").content("first Content").build(),
						ReactivePost.builder().title("Jane").content("second Content").build()));

		Flux<ReactivePost> composite = this.service.findAllPosts().thenMany(saved);

		StepVerifier.create(composite).expectNextCount(3).verifyComplete();
	}

	@Test
	void save() {
		Mono<ReactivePost> reactivePostMono = this.service
				.savePost(ReactivePost.builder().title("Raja").content("fourth Content").build());

		StepVerifier.create(reactivePostMono)
				.expectNextMatches(saved -> StringUtils.hasText(String.valueOf(saved.getId()))
						&& saved.getTitle().equalsIgnoreCase("Raja")
						&& saved.getContent().equalsIgnoreCase("fourth Content"))
				.verifyComplete();
	}

	@Test
	void delete() {

		ReactivePost test = ReactivePost.builder().title("Hello").content("fifth Content").build();
		Mono<ReactivePost> deleted = this.service.savePost(test)
				.flatMap(saved -> this.service.deletePostById(saved.getId()));
		StepVerifier.create(deleted)
				.expectNextMatches(reactivePost -> reactivePost.getContent().equalsIgnoreCase(test.getContent())
						&& reactivePost.getTitle().equalsIgnoreCase(test.getTitle()))
				.verifyComplete();
	}

	@Test
	void update() {
		ReactivePost test = ReactivePost.builder().title("Junit").content("sixth Content").build();
		ReactivePost test1 = ReactivePost.builder().title("Junit1").content("sixth Content").build();

		Mono<ReactivePost> saved = this.service.savePost(test).flatMap(p -> this.service.update(p.getId(), test1));

		StepVerifier.create(saved).expectNextMatches(reactivePost -> reactivePost.getTitle().equalsIgnoreCase("Junit1"))
				.verifyComplete();
	}

	@Test
	void getById() {
		ReactivePost test = ReactivePost.builder().title("Junit_new").content("seventh Content").build();
		Mono<ReactivePost> deleted = this.service.savePost(test)
				.flatMap(saved -> this.service.findPostById(saved.getId()));
		StepVerifier.create(deleted)
				.expectNextMatches(reactivePost -> StringUtils.hasText(String.valueOf(reactivePost.getId()))
						&& test.getTitle().equalsIgnoreCase(reactivePost.getTitle()))
				.verifyComplete();
	}

}
