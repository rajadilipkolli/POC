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
import java.util.Optional;

import javax.validation.Valid;

import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

	private final PostService postService;

	@GetMapping("/{user_name}/posts")
	public ResponseEntity<List<PostDTO>> getPostsByUserName(@PathVariable("user_name") String userName) {
		return ResponseEntity.of(Optional.of(this.postService.fetchAllPostsByUserName(userName)));
	}

	@PostMapping("/{user_name}/posts/")
	public ResponseEntity<Object> createPostByUserName(@RequestBody @Valid PostDTO postDTO,
			@PathVariable("user_name") String userName, UriComponentsBuilder ucBuilder) {
		postDTO.setCreatedBy(userName);
		this.postService.createPost(postDTO);

		return ResponseEntity.created(ucBuilder.path("/users/{user_name}/posts").buildAndExpand(userName).toUri())
				.build();
	}

	@PutMapping("/{user_name}/posts/{title}")
	public ResponseEntity<PostDTO> updatePostByUserName(@RequestBody @Valid PostDTO postDTO,
			@PathVariable("user_name") String userName, @PathVariable("title") String title) {
		postDTO.setCreatedBy(userName);
		final PostDTO updatedPost = this.postService.updatePostByUserNameAndId(postDTO, title);
		return ResponseEntity.ok(updatedPost);
	}

	@DeleteMapping("/{user_name}/posts/{title}")
	public ResponseEntity<Void> deletePostByUserName(@PathVariable("user_name") String userName,
			@PathVariable("title") String title) {
		this.postService.deletePostByIdAndUserName(userName, title);
		return ResponseEntity.accepted().build();
	}

}
