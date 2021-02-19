package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;

public class Records {

    public record RootValueDTO(String title, String content) {}

    public record PostsDTO(@JsonProperty("postList") List<PostDTO> postList) {}

    public record PostCommentsDTO(@JsonProperty("review") String review) {}

    public record TagDTO(@JsonProperty("name") String name) {}

    public record PostRequestDTO(
            @JsonProperty("title") @NotBlank(message = "Title of post is mandatory") String title,
            @JsonProperty("content") @NotBlank(message = "Context of post can't be Blank")
                    String content,
            @JsonProperty("comments") List<Records.PostCommentsDTO> comments,
            @JsonProperty("tags") List<Records.TagDTO> tags) {}
}
