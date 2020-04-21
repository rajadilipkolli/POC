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

package com.poc.restfulpoc.config.batch;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.poc.restfulpoc.dto.PostCommentProjection;
import com.poc.restfulpoc.dto.PostCommentsDTO;
import com.poc.restfulpoc.dto.PostDTO;
import com.poc.restfulpoc.dto.Records.RootValueDTO;
import com.poc.restfulpoc.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomItemProcessor implements ItemProcessor<List<Long>, List<PostDTO>> {

	private final PostRepository postRepository;

	final Function<Entry<RootValueDTO, List<PostCommentsDTO>>, PostDTO> mapToPostDTO = entry -> PostDTO.builder()
			.title(entry.getKey().title()).content(entry.getKey().content()).comments(entry.getValue()).build();

	final Function<PostCommentProjection, RootValueDTO> titleAndContentClassifier = postCommentProjection -> new RootValueDTO(
			postCommentProjection.getTitle(), postCommentProjection.getContent());

	final Function<PostCommentProjection, PostCommentsDTO> mapToPostComments = postCommentProjection -> PostCommentsDTO
			.builder().review(postCommentProjection.getReview()).build();

	final Collector<PostCommentProjection, ?, List<PostCommentsDTO>> downStreamCollector = Collectors
			.mapping(this.mapToPostComments, Collectors.toList());

	@Override
	public List<PostDTO> process(@NonNull List<Long> items) {

		List<PostCommentProjection> postCommentProjections = this.postRepository.findByIds(items);

		return postCommentProjections.stream()
				.collect(Collectors.groupingBy(this.titleAndContentClassifier, this.downStreamCollector)).entrySet()
				.stream().map(this.mapToPostDTO).collect(Collectors.toUnmodifiableList());
	}

}
