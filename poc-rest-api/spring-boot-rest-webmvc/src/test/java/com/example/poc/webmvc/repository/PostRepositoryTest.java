/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.repository;

import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractPostgreSQLContainerBase;
import com.example.poc.webmvc.dto.PostCommentProjection;
import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.RootValueDTO;
import com.example.poc.webmvc.entities.Post;
import com.example.poc.webmvc.entities.PostComment;
import com.example.poc.webmvc.entities.PostDetails;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest extends AbstractPostgreSQLContainerBase {

    @Autowired private PostRepository postRepository;

    private Post persistedPost;

    @BeforeAll
    void init() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Post post = new Post();
        post.setUpdatedOn(currentDateTime);
        post.setTitle("Post Title");
        post.setContent("Post Content");
        PostDetails postDetails = new PostDetails();
        postDetails.setCreatedBy("JUNIT");
        post.addDetails(postDetails);
        PostComment postCommentOld = new PostComment();
        postCommentOld.setCreatedOn(currentDateTime.minusDays(1));
        postCommentOld.setReview("Review Old");
        post.addComment(postCommentOld);
        PostComment postCommentNew = new PostComment();
        postCommentNew.setCreatedOn(currentDateTime);
        postCommentNew.setReview("Review New");
        post.addComment(postCommentNew);
        this.persistedPost = this.postRepository.save(post);
    }

    @AfterAll
    void destroy() {
        this.postRepository.delete(this.persistedPost);
    }

    @Test
    void testProjection() {

        List<PostCommentProjection> postCommentProjections =
                this.postRepository.findByTitle("Post Title");

        final Function<Map.Entry<RootValueDTO, List<PostCommentsDTO>>, PostDTO> mapToPostDTO =
                entry ->
                        PostDTO.builder()
                                .title(entry.getKey().title())
                                .content(entry.getKey().content())
                                .comments(entry.getValue())
                                .build();
        final Function<PostCommentProjection, RootValueDTO> titleAndContentClassifier =
                postCommentProjection ->
                        new RootValueDTO(
                                postCommentProjection.getTitle(),
                                postCommentProjection.getContent());
        final Function<PostCommentProjection, PostCommentsDTO> mapToPostComments =
                postCommentProjection -> new PostCommentsDTO(postCommentProjection.getReview());
        final Collector<PostCommentProjection, ?, List<PostCommentsDTO>> downStreamCollector =
                Collectors.mapping(mapToPostComments, Collectors.toList());

        List<PostDTO> postDTOS =
                postCommentProjections.stream()
                        .collect(groupingBy(titleAndContentClassifier, downStreamCollector))
                        .entrySet()
                        .stream()
                        .map(mapToPostDTO)
                        .toList();

        assertThat(postDTOS).isNotEmpty().hasSize(1);
        PostDTO postDTO = postDTOS.get(0);
        assertThat(postDTO.getTitle()).isEqualTo("Post Title");
        assertThat(postDTO.getContent()).isEqualTo("Post Content");
        assertThat(postDTO.getComments()).isNotEmpty().hasSizeGreaterThanOrEqualTo(2);
        assertThat(postDTO.getComments())
                .contains(new PostCommentsDTO("Review New"), new PostCommentsDTO("Review Old"));
    }

    @Test
    void shouldReturnPostWhenUserNameIsPassed() {
        List<Post> postList = this.postRepository.findByDetailsCreatedBy("JUNIT");
        assertThat(postList).isNotEmpty().hasSize(1);
        assertThat(postList.get(0).getDetails().getCreatedBy()).isEqualTo("JUNIT");
    }
}
