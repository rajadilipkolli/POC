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

import com.poc.reactivepoc.entity.ReactivePost;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactivePOCTest {

	@LocalServerPort
	private int port;

	private WebTestClient webClient;

	@BeforeAll
	public void setup() {
		this.webClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + this.port).build();
	}

	@Test
	public void willLoadPosts() {
		this.webClient.get().uri("/posts").exchange().expectStatus().is2xxSuccessful()
				.expectBodyList(ReactivePost.class).hasSize(1);
	}

}
