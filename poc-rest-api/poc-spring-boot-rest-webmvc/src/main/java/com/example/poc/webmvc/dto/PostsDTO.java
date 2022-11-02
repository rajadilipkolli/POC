package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PostsDTO(@JsonProperty("postList") List<PostDTO> postList) {}
