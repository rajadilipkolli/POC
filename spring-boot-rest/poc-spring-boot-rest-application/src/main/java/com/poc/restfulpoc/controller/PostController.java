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

import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/{userName}")
	public ResponseEntity<List<PostDTO>> getPostsByUserName(@PathVariable("userName") String userName) {
		return ResponseEntity.of(Optional.of(this.postService.fetchAllPostsByUserName(userName)));
	}

}
