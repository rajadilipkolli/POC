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
package com.poc.restfulpoc.mapper;

import java.util.List;

import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.TagDTO;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import com.poc.restfulpoc.entities.PostDetails;
import com.poc.restfulpoc.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

	List<PostDTO> mapToPostDTOs(List<Post> postList);

	@Mapping(target = "createdBy", source = "details.createdBy")
	@Mapping(target = "createdOn", source = "details.createdOn")
	PostDTO mapPostToDTO(Post post);

	@Mapping(target = "comments", ignore = true)
	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "details", ignore = true)
	Post postDtoToPostIgnoringChild(PostDTO postDTO);

	PostComment postCommentsDTOToPostComment(PostCommentsDTO postCommentsDTO);

	Tag tagDTOToTag(TagDTO tagDTO);

	PostDetails postDTOToPostDetails(PostDTO postDTO);

	@Mapping(target = "createdOn", ignore = true)
	void updateReferenceValues(PostDTO postDTO, @MappingTarget Post post);

	default Post postDtoToPost(PostDTO postDTO) {
		if (postDTO == null) {
			return null;
		}

		Post post = postDtoToPostIgnoringChild(postDTO);
		post.addDetails(postDTOToPostDetails(postDTO));
		addPostCommentsToPost(postDTO.getComments(), post);
		addPostTagsToPost(postDTO.getTags(), post);

		return post;
	}

	default void addPostTagsToPost(List<TagDTO> tags, Post post) {
		if (tags == null) {
			return;
		}

		for (TagDTO tagDTO : tags) {
			post.addTag(tagDTOToTag(tagDTO));
		}
	}

	default void addPostCommentsToPost(List<PostCommentsDTO> comments, Post post) {
		if (comments == null) {
			return;
		}

		for (PostCommentsDTO postCommentsDTO : comments) {
			post.addComment(postCommentsDTOToPostComment(postCommentsDTO));
		}
	}

}
