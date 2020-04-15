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
import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.RootValueDTO;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import com.poc.restfulpoc.entities.PostDetails;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private PostRepository postRepository;

	private Post persistedPost;

	@BeforeAll
	void init() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		Post post = new Post();
		post.setCreatedOn(currentDateTime);
		post.setTitle("Post Title");
		post.setContent("Post Content");
		PostDetails postDetails = new PostDetails();
		postDetails.setCreatedBy("JUNIT");
		post.addDetails(postDetails);
		PostComment postCommentOld = new PostComment();
		postCommentOld.setCreatedOn(currentDateTime.minusDays(1));
		postCommentOld.setReview("Review Old");
		post.addComment(postCommentOld);
		PostComment postCommentNew = new PostComment();
		postCommentNew.setCreatedOn(currentDateTime);
		postCommentNew.setReview("Review New");
		post.addComment(postCommentNew);
		this.persistedPost = this.postRepository.save(post);
	}

	@AfterAll
	void destroy() {
		this.postRepository.delete(this.persistedPost);
	}

	@Test
	void testProjection() {

		List<PostCommentProjection> postCommentProjections = this.postRepository.findByTitle("Post Title");

		final Function<Entry<RootValueDTO, List<PostCommentsDTO>>, PostDTO> mapToPostDTO = entry -> PostDTO.builder()
				.title(entry.getKey().getTitle()).content(entry.getKey().getContent()).comments(entry.getValue())
				.build();
		final Function<PostCommentProjection, RootValueDTO> titleAndContentClassifier = postCommentProjection -> new RootValueDTO(
				postCommentProjection.getTitle(), postCommentProjection.getContent());
		final Function<PostCommentProjection, PostCommentsDTO> mapToPostComments = postCommentProjection -> PostCommentsDTO
				.builder().review(postCommentProjection.getReview()).build();
		final Collector<PostCommentProjection, ?, List<PostCommentsDTO>> downStreamCollector = Collectors
				.mapping(mapToPostComments, Collectors.toList());

		List<PostDTO> postDTOS = postCommentProjections.stream()
				.collect(groupingBy(titleAndContentClassifier, downStreamCollector)).entrySet().stream()
				.map(mapToPostDTO).collect(toUnmodifiableList());

		assertThat(postDTOS).isNotEmpty().hasSize(1);
		PostDTO postDTO = postDTOS.get(0);
		assertThat(postDTO.getTitle()).isEqualTo("Post Title");
		assertThat(postDTO.getContent()).isEqualTo("Post Content");
		assertThat(postDTO.getComments()).isNotEmpty().hasSizeGreaterThanOrEqualTo(2);
		assertThat(postDTO.getComments()).contains(PostCommentsDTO.builder().review("Review New").build(),
				PostCommentsDTO.builder().review("Review Old").build());

	}

	@Test
	void shouldReturnPostWhenUserNameIsPassed() {
		List<Post> postList = this.postRepository.findByDetailsCreatedBy("JUNIT");
		assertThat(postList).isNotEmpty().hasSize(1);
		assertThat(postList.get(0).getDetails().getCreatedBy()).isEqualTo("JUNIT");
	}

}
