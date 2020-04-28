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
package com.poc.restfulpoc.util;

import java.util.List;

import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.TagDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockObjectCreator {

	public PostDTO getPostDTO() {
		PostDTO postDto = new PostDTO();
		postDto.setCreatedBy("junit");
		postDto.setContent("junitContent");
		postDto.setTitle("junitTitle");
		postDto.setComments(List.of(getPostComments()));
		postDto.setTags(List.of(getTag()));
		return postDto;
	}

	private TagDTO getTag() {
		return new TagDTO("tag");
	}

	private PostCommentsDTO getPostComments() {
		return new PostCommentsDTO("junitReview");
	}

}
