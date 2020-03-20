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

package com.poc.reactivepoc.dataload;

import java.time.LocalDateTime;

import com.poc.reactivepoc.repository.PostRepository;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

	private final PostRepository postRepository;

	@EventListener(ContextRefreshedEvent.class)
	public void init() {
		log.info("start data initialization  ...");
		Post post = new Post();
		post.setTitle("First post title");
		post.setContent("Content of my first post");
		post.setCreatedOn(LocalDateTime.now());
		PostDetails postDetails = new PostDetails();
		postDetails.setCreatedBy("Junit");
		post.setDetails(postDetails);
		this.postRepository.save(post).map(post1 -> post.getId()).log().thenMany(this.postRepository.findAll().log())
				.subscribe(null, null, () -> log.info("initialization is done..."));
	}

}
