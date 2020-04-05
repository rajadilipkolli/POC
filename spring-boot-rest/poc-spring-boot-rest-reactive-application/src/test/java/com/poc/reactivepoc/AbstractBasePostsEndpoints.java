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
package com.poc.reactivepoc;

import java.util.UUID;

import com.poc.reactivepoc.entity.ReactivePost;
import com.poc.reactivepoc.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;

@Slf4j
@WebFluxTest
public abstract class AbstractBasePostsEndpoints {

	private final WebTestClient client; // <2>

	@MockBean // <3>
	private PostRepository repository;

	public AbstractBasePostsEndpoints(WebTestClient client) {
		this.client = client;
	}

	@Test
	public void getAll() {

		log.info("running  " + this.getClass().getName());

		// <4>
		given(this.repository.findAll()).willReturn(Flux.just(ReactivePost.builder().id(1).content("A").build(),
				ReactivePost.builder().id(2).content("B").build()));

		// <5>
		this.client.get().uri("/posts").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.[0].id").isEqualTo("1")
				.jsonPath("$.[0].content").isEqualTo("A").jsonPath("$.[1].id").isEqualTo("2").jsonPath("$.[1].content")
				.isEqualTo("B");
	}

	@Test
	public void save() {
		ReactivePost data = ReactivePost.builder().id(123).content(UUID.randomUUID().toString()).build();
		given(this.repository.save(Mockito.any(ReactivePost.class))).willReturn(Mono.just(data));
		MediaType jsonUtf8 = MediaType.APPLICATION_JSON;
		this.client.post().uri("/posts").contentType(jsonUtf8).body(Mono.just(data), ReactivePost.class).exchange()
				.expectStatus().isCreated().expectHeader().contentType(jsonUtf8);
	}

	@Test
	public void delete() {
		ReactivePost data = ReactivePost.builder().id(123).content(UUID.randomUUID().toString()).build();
		Mockito.when(this.repository.findById(data.getId())).thenReturn(Mono.just(data));
		Mockito.when(this.repository.deleteById(data.getId())).thenReturn(Mono.empty());
		this.client.delete().uri("/posts/" + data.getId()).exchange().expectStatus().isOk();
	}

	@Test
	public void update() {
		ReactivePost data = ReactivePost.builder().id(123).content(UUID.randomUUID().toString()).build();

		Mockito.when(this.repository.findById(data.getId())).thenReturn(Mono.just(data));

		Mockito.when(this.repository.save(data)).thenReturn(Mono.just(data));

		this.client.put().uri("/posts/" + data.getId()).contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(data), ReactivePost.class).exchange().expectStatus().isOk();
	}

	@Test
	public void getById() {

		ReactivePost data = ReactivePost.builder().id(1).content("A").build();

		Mockito.when(this.repository.findById(data.getId())).thenReturn(Mono.just(data));

		this.client.get().uri("/posts/" + data.getId()).accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
				.isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.id")
				.isEqualTo(data.getId()).jsonPath("$.content").isEqualTo(data.getContent());
	}

}
