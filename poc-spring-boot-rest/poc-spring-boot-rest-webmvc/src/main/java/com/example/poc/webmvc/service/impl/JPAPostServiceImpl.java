package com.example.poc.webmvc.service.impl;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.exception.PostNotFoundException;
import com.example.poc.webmvc.mapper.PostMapper;
import com.example.poc.webmvc.repository.PostRepository;
import com.example.poc.webmvc.service.PostService;
import com.example.poc.webmvc.entities.Post;
import java.util.List;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("jpaPostService")
@Primary
@RequiredArgsConstructor
public class JPAPostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final BiFunction<String, String, PostNotFoundException> supplierBiFunction =
            (userName, title) ->
                    new PostNotFoundException(
                            String.format(
                                    "Post with title %s for user %s not found", title, userName));

    @Transactional(readOnly = true)
    @Override
    public List<PostDTO> fetchAllPostsByUserName(String userName) {
        // https://vladmihalcea.com/hibernate-multiplebagfetchexception/
        // Key is to run in same transaction
        List<Post> postList = this.postRepository.findByDetailsCreatedBy(userName);
        List<Post> fullPosts = this.postRepository.findPostsWithAllDetails(postList);
        return this.postMapper.mapToPostDTOs(fullPosts);
    }

    @Override
    @Cacheable(value = "posts", key = "#userName+#title", unless = "#result == null")
    public PostDTO fetchPostByUserNameAndTitle(String userName, String title) {

        Post post =
                this.postRepository
                        .findByDetailsCreatedByAndTitle(userName, title)
                        .orElseThrow(() -> supplierBiFunction.apply(userName, title));
        return this.postMapper.mapPostToDTO(post);
    }

    @Transactional
    @Override
    @CacheEvict(value = "posts", key = "#userName+#title")
    public void deletePostByIdAndUserName(String userName, String title) {
        this.postRepository.deleteByTitleAndDetailsCreatedBy(title, userName);
    }

    @Transactional
    @Override
    public PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title) {
        // https://vladmihalcea.com/hibernate-multiplebagfetchexception/
        // Key is to run in same transaction
        Post post =
                this.postRepository
                        .findByDetailsCreatedByAndTitle(postDTO.getCreatedBy(), title)
                        .orElseThrow(() -> supplierBiFunction.apply(postDTO.getCreatedBy(), title));
        this.postMapper.updateReferenceValues(postDTO, post);
        return this.postMapper.mapPostToDTO(this.postRepository.save(post));
    }

    @Override
    @Transactional
    public void createPost(PostRequestDTO postRequestDTO, String userName) {
        PostDTO postDTO = this.postMapper.postRequestDtoToPostDto(postRequestDTO);
        postDTO.setCreatedBy(userName);
        Post post = this.postMapper.postDtoToPost(postDTO);
        this.postRepository.save(post);
    }
}
