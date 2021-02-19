package com.example.poc.webmvc.common;

import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.Records;
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

    private Records.TagDTO getTag() {
        return new Records.TagDTO("tag");
    }

    private Records.PostCommentsDTO getPostComments() {
        return new Records.PostCommentsDTO("junitReview");
    }
}
