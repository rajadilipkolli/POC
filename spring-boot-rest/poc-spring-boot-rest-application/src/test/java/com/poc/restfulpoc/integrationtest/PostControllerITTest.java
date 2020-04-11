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
package com.poc.restfulpoc.integrationtest;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.TagDTO;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostControllerITTest extends AbstractRestFulPOCApplicationTest {

	@Test
	void testFetchingPostsByUserId() {
		ResponseEntity<PostDTO[]> response = userRestTemplate().getForEntity("/posts/raja", PostDTO[].class);
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull().hasSize(1);
		PostDTO postDTO = response.getBody()[0];
		assertThat(postDTO.getComments()).isNotEmpty().hasSize(2).contains(
				PostCommentsDTO.builder().review("Excellent").build(),
				PostCommentsDTO.builder().review("Good").build());
		assertThat(postDTO.getTags()).isNotEmpty().hasSize(2).contains(new TagDTO("Java"), new TagDTO("Spring Boot"));
		assertThat(postDTO.getCreatedBy()).isEqualTo("raja");
		assertThat(postDTO.getTitle()).isEqualTo("A Beautiful Post in Java");
	}

}
