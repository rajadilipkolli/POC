/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.service;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import java.util.List;

public interface PostService {

    List<PostDTO> fetchAllPostsByUserName(String userName);

    PostDTO fetchPostByUserNameAndTitle(String userName, String title);

    void deletePostByIdAndUserName(String userName, String title);

    PostDTO updatePostByUserNameAndId(PostDTO postDTO, String title);

    void createPost(PostRequestDTO postRequestDTO, String userName);
}
