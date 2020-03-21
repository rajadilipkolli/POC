/*
 * Copyright 2018 the original author or authors.
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
package com.poc.reactivepoc.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.poc.reactivepoc.entity.ReactivePost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.DatabaseClient;

@DataR2dbcTest
public class ReactivePostRepositoryTest {

	@Autowired
	PostRepository postRepository;

	@Autowired
	DatabaseClient database;

	@BeforeEach
	public void setUp() {

		Hooks.onOperatorDebug();

		List<String> statements = Arrays.asList(//
				"DROP TABLE IF EXISTS reactive_posts;",
				"CREATE TABLE reactive_posts ( id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL, content VARCHAR(100) NOT NULL);");

		statements.forEach(it -> this.database.execute(it) //
				.fetch() //
				.rowsUpdated() //
				.as(StepVerifier::create) //
				.expectNextCount(1) //
				.verifyComplete());
	}

	@Test
	public void executesFindAll() throws IOException {

		ReactivePost dave = new ReactivePost(null, "Dave", "Matthews");
		ReactivePost carter = new ReactivePost(null, "Carter", "Beauford");

		insertPosts(dave, carter);

		this.postRepository.findAll() //
				.as(StepVerifier::create) //
				.assertNext(dave::equals) //
				.assertNext(carter::equals) //
				.verifyComplete();
	}

	@Test
	public void executesAnnotatedQuery() throws IOException {

		ReactivePost dave = new ReactivePost(null, "Dave", "Matthews");
		ReactivePost carter = new ReactivePost(null, "Carter", "Beauford");

		insertPosts(dave, carter);

		this.postRepository.findByContent("Matthews") //
				.as(StepVerifier::create) //
				.assertNext(dave::equals) //
				.verifyComplete();
	}

	private void insertPosts(ReactivePost... reactivePosts) {

		this.postRepository.saveAll(Arrays.asList(reactivePosts))//
				.as(StepVerifier::create) //
				.expectNextCount(2) //
				.verifyComplete();
	}

}
