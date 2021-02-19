package com.example.poc.webmvc.service;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.Records;
import java.util.List;

public interface PostService {

    List<PostDTO> fetchAllPostsByUserName(String userName);

    PostDTO fetchPostByUserNameAndTitle(String userName, String title);

    void deletePostByIdAndUserName(String userName, String title);

    PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title);

    void createPost(Records.PostRequestDTO postRequestDTO, String userName);
}
