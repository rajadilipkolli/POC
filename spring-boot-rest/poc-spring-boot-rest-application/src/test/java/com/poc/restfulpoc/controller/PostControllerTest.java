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
package com.poc.restfulpoc.controller;

import java.util.List;

import com.poc.restfulpoc.service.PostService;
import com.poc.restfulpoc.util.MockObjectCreator;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@WithMockUser(roles = "USER")
class PostControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PostService postService;

	@Test
	void shouldReturnAllPostsWithLinks() throws Exception {
		given(this.postService.fetchAllPostsByUserName("junit")).willReturn(List.of(MockObjectCreator.getPostDTO()));

		this.mvc.perform(get("/users/junit/posts").accept(MediaTypes.HAL_JSON_VALUE)) //
				.andDo(print()) //
				.andExpect(status().isOk()) //
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) //
				.andExpect(jsonPath("$.postList[0].title", is("junitTitle"))) //
				.andExpect(jsonPath("$.postList[0].content", is("junitContent"))) //
				.andExpect(jsonPath("$.postList[0].createdBy", is("junit"))) //
				.andExpect(jsonPath("$.postList[0].createdOn", nullValue())) //
				.andExpect(jsonPath("$.postList[0].comments.size()", is(1))) //
				.andExpect(jsonPath("$.postList[0].comments[0].review", is("junitReview"))) //
				.andExpect(jsonPath("$.postList[0].tags.size()", is(1))) //
				.andExpect(jsonPath("$.postList[0].tags.[0].name", is("tag"))) //
				.andExpect(jsonPath("$.postList[0].links.size()", is(1))) //
				.andExpect(jsonPath("$.postList[0].links[0].rel", is("self"))) //
				.andExpect(jsonPath("$.postList[0].links[0].href", is("http://localhost/users/junit/posts/junitTitle")))
				.andReturn();
	}

}
