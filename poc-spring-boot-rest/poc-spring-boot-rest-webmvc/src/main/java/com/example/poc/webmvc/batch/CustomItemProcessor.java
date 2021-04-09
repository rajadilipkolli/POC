package com.example.poc.webmvc.batch;

import com.example.poc.webmvc.dto.PostCommentProjection;
import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.repository.PostRepository;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomItemProcessor implements ItemProcessor<List<Long>, List<PostDTO>> {

    private final PostRepository postRepository;

    final Function<Entry<String, List<PostCommentsDTO>>, PostDTO> mapToPostDTO =
            entry -> PostDTO.builder().title(entry.getKey()).comments(entry.getValue()).build();

    final Function<PostCommentProjection, String> titleClassifier = PostCommentProjection::getTitle;

    final Function<PostCommentProjection, PostCommentsDTO> mapToPostComments =
            postCommentProjection -> new PostCommentsDTO(postCommentProjection.getReview());

    final Collector<PostCommentProjection, ?, List<PostCommentsDTO>> downStreamCollector =
            Collectors.mapping(this.mapToPostComments, Collectors.toList());

    @Override
    public List<PostDTO> process(List<Long> items) {

        List<PostCommentProjection> postCommentProjections = this.postRepository.findByIds(items);

        return postCommentProjections.stream()
                .collect(Collectors.groupingBy(this.titleClassifier, this.downStreamCollector))
                .entrySet()
                .stream()
                .map(this.mapToPostDTO)
                .collect(Collectors.toUnmodifiableList());
    }
}
