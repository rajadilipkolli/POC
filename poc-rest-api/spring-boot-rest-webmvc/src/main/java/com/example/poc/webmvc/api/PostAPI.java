/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.api;

import com.example.poc.webmvc.dto.PostRequestDTO;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(
        tags = {@Tag(name = "Post API", description = "performs all CRUD operations for posts")})
public interface PostAPI {

    @Operation(summary = "Creates post")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Post Created",
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                @ApiResponse(
                        responseCode = "419",
                        description = "Post with same title exists",
                        content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
            })
    @Tag(name = "Post API")
    ResponseEntity<Object> createPostByUserName(
            PostRequestDTO postRequestDTO,
            @Parameter(description = "id of user who is creating") String userName,
            UriComponentsBuilder ucBuilder);
}
