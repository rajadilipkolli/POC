package com.poc.restfulpoc.util;

import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.TagDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

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
