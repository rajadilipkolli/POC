/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PostsDTO(@JsonProperty("postList") List<PostDTO> postList) {}
