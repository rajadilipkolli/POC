package com.example.poc.webmvc.service;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.mapper.PostMapper;
import com.example.poc.webmvc.repository.PostRepository;
import com.poc.restfulpoc.entities.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<PostDTO> fetchAllPostsByUserName(String userName) {
        // https://vladmihalcea.com/hibernate-multiplebagfetchexception/
        // Key is to run in same transaction
        List<Post> postList = this.postRepository.findByDetailsCreatedBy(userName);
        List<Post> fullPosts = this.postRepository.findPostsWithAllDetails(postList);
        return this.postMapper.mapToPostDTOs(fullPosts);
    }

    public PostDTO fetchPostByUserNameAndTitle(String userName, String title) {
        Post post = this.postRepository.findByDetailsCreatedByAndTitle(userName, title);
        return this.postMapper.mapPostToDTO(post);
    }

    @Transactional
    public void createPost(PostDTO postDTO) {
        Post post = this.postMapper.postDtoToPost(postDTO);
        this.postRepository.save(post);
    }

    @Transactional
    public void deletePostByIdAndUserName(String userName, String title) {
        this.postRepository.deleteByTitleAndDetailsCreatedBy(title, userName);
    }

    @Transactional
    public PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title) {
        // https://vladmihalcea.com/hibernate-multiplebagfetchexception/
        // Key is to run in same transaction
        Post post =
                this.postRepository.findByDetailsCreatedByAndTitle(postDTO.getCreatedBy(), title);
        this.postMapper.updateReferenceValues(postDTO, post);
        return this.postMapper.mapPostToDTO(this.postRepository.save(post));
    }
}
