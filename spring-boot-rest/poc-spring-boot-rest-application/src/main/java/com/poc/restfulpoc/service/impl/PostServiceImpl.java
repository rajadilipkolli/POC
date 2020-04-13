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
package com.poc.restfulpoc.service.impl;

import java.util.Collections;
import java.util.List;

import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.mapper.PostMapper;
import com.poc.restfulpoc.repository.PostRepository;
import com.poc.restfulpoc.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;

	private final PostMapper postMapper;

	@Override
	@Transactional(readOnly = true)
	public List<PostDTO> fetchAllPostsByUserName(String userName) {
		// https://vladmihalcea.com/hibernate-multiplebagfetchexception/
		// Key is to run in same transaction
		List<Post> postList = this.postRepository.findByDetailsCreatedBy(userName);
		List<Post> fullPosts = this.postRepository.findPostsWithAllDetails(postList);
		return this.postMapper.mapToPostDTOs(fullPosts);
	}

	@Override
	@Transactional(readOnly = true)
	public PostDTO fetchPostByUserNameAndTitle(String userName, String title) {
		Post postWithComments = this.postRepository.findByDetailsCreatedByAndTitle(userName, title);
		Post post = this.postRepository.findPostsWithAllDetails(Collections.singletonList(postWithComments)).get(0);
		return this.postMapper.mapPostToDTO(post);
	}

	@Override
	@Transactional
	public void createPost(PostDTO postDTO) {
		Post post = this.postMapper.postDtoToPost(postDTO);
		this.postRepository.save(post);
	}

	@Override
	@Transactional
	public void deletePostByIdAndUserName(String userName, String title) {
		this.postRepository.deleteByTitleAndDetailsCreatedBy(title, userName);
	}

	@Override
	@Transactional
	public PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title) {
		// https://vladmihalcea.com/hibernate-multiplebagfetchexception/
		// Key is to run in same transaction
		Post postWithComments = this.postRepository.findByDetailsCreatedByAndTitle(postDTO.getCreatedBy(), title);
		Post post = this.postRepository.findPostsWithAllDetails(Collections.singletonList(postWithComments)).get(0);
		this.postMapper.updateReferenceValues(postDTO, post);
		return this.postMapper.mapPostToDTO(this.postRepository.save(post));
	}

}
