package com.example.poc.webmvc.api;

import com.example.poc.webmvc.dto.Records;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface PostAPI {

    @Operation(summary = "Creates post")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Post Created",
                        content = {@Content(mediaType = "application/json")}),
                @ApiResponse(
                        responseCode = "419",
                        description = "Post with same title exists",
                        content = {@Content(mediaType = "application/json")})
            })
    ResponseEntity<Object> createPostByUserName(
            Records.PostRequestDTO postRequestDTO,
            @Parameter(description = "id of user who is creating") String userName,
            UriComponentsBuilder ucBuilder);
}
