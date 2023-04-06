/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostCommentProjection;
import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("customItemProcessor")
public class CustomItemProcessor implements ItemProcessor<List<Long>, List<PostDTO>> {

    private final PostRepository postRepository;

    private final Function<Entry<String, List<PostCommentsDTO>>, PostDTO> mapToPostDTO =
            entry -> PostDTO.builder().title(entry.getKey()).comments(entry.getValue()).build();

    private final Function<PostCommentProjection, String> titleClassifier =
            PostCommentProjection::getTitle;

    private final Function<PostCommentProjection, PostCommentsDTO> mapToPostComments =
            postCommentProjection -> new PostCommentsDTO(postCommentProjection.getReview());

    private final Collector<PostCommentProjection, ?, List<PostCommentsDTO>> downStreamCollector =
            Collectors.mapping(this.mapToPostComments, Collectors.toList());

    @Override
    public List<PostDTO> process(List<Long> postIds) {

        List<PostCommentProjection> postCommentProjections = this.postRepository.findByIds(postIds);

        return postCommentProjections.stream()
                .collect(Collectors.groupingBy(this.titleClassifier, this.downStreamCollector))
                .entrySet()
                .stream()
                .map(this.mapToPostDTO)
                .toList();
    }
}
