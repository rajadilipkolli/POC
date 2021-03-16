package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;

public record PostRequestDTO(
        @JsonProperty("title") @NotBlank(message = "Title of post is mandatory") String title,
        @JsonProperty("content") @NotBlank(message = "Context of post can't be Blank")
                String content,
        @JsonProperty("comments") List<PostCommentsDTO> comments,
        @JsonProperty("tags") List<TagDTO> tags) {}
