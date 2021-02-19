package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Records {

    public record RootValueDTO(String title, String content) {}

    public record PostsDTO(@JsonProperty("postList") List<PostDTO> postList) {}

    public record PostCommentsDTO(@JsonProperty("review") String review) {}

    public record TagDTO(@JsonProperty("name") String name) {}
}
