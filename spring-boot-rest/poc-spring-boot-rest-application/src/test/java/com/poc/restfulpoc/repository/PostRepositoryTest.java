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

package com.poc.restfulpoc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.dto.PostCommentProjection;
import com.poc.restfulpoc.dto.PostComments;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private PostRepository postRepository;

	@Test
	void testProjection() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		Post post = new Post();
		post.setId(10_000L);
		post.setCreatedOn(currentDateTime);
		post.setTitle("Post Title");
		PostComment postCommentOld = new PostComment();
		postCommentOld.setId(10_000L);
		postCommentOld.setCreatedOn(currentDateTime.minusDays(1));
		postCommentOld.setReview("Review Old");
		post.addComment(postCommentOld);
		PostComment postCommentNew = new PostComment();
		postCommentNew.setId(10_001L);
		postCommentNew.setCreatedOn(currentDateTime);
		postCommentNew.setReview("Review New");
		post.addComment(postCommentNew);
		this.postRepository.save(post);

		List<PostCommentProjection> postCommentProjections = this.postRepository.findByTitle("Post Title");

		final Function<Entry<String, List<PostComments>>, PostDTO> mapToPostDTO = entry -> PostDTO.builder()
				.title(entry.getKey()).comments(entry.getValue()).build();
		final Function<PostCommentProjection, String> titleClassifier = PostCommentProjection::getTitle;
		final Function<PostCommentProjection, PostComments> mapToPostComments = postCommentProjection -> PostComments
				.builder().review(postCommentProjection.getReview()).build();
		final Collector<PostCommentProjection, ?, List<PostComments>> downStreamCollector = Collectors
				.mapping(mapToPostComments, Collectors.toList());

		List<PostDTO> postDTOS = postCommentProjections.stream()
				.collect(groupingBy(titleClassifier, downStreamCollector)).entrySet().stream().map(mapToPostDTO)
				.collect(toUnmodifiableList());

		assertThat(postDTOS).isNotEmpty().hasSize(1);
		PostDTO postDTO = postDTOS.get(0);
		assertThat(postDTO.getTitle()).isEqualTo("Post Title");
		assertThat(postDTO.getComments()).isNotEmpty().hasSize(2);
		assertThat(postDTO.getComments()).contains(PostComments.builder().review("Review New").build(),
				PostComments.builder().review("Review Old").build());
	}

}
