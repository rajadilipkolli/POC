/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.common;

import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.TagDTO;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockObjectCreator {

    public PostDTO getPostDTO() {
        PostDTO postDto = new PostDTO();
        postDto.setCreatedBy("junit");
        postDto.setContent("junitContent");
        postDto.setTitle("junitTitle");
        postDto.setComments(List.of(getPostComments()));
        postDto.setTags(List.of(getTag()));
        return postDto;
    }

    private TagDTO getTag() {
        return new TagDTO("tag");
    }

    private PostCommentsDTO getPostComments() {
        return new PostCommentsDTO("junitReview");
    }
}
