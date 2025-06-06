/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.controller;

import com.example.poc.webmvc.api.PostAPI;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostRequestDTO;
import com.example.poc.webmvc.dto.PostsDTO;
import com.example.poc.webmvc.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
public class PostController implements PostAPI {

    private final PostService jpaPostService;
    private final PostService jooqPostService;

    private final BiFunction<String, PostDTO, PostDTO> addLinkToPostBiFunction =
            (userName, postDTO) -> {
                Link linkTo =
                        WebMvcLinkBuilder.linkTo(
                                        WebMvcLinkBuilder.methodOn(PostController.this.getClass())
                                                .getPostByUserNameAndTitle(
                                                        userName, postDTO.getTitle()))
                                .withSelfRel();
                postDTO.add(linkTo);
                return postDTO;
            };

    public PostController(
            @Qualifier("jpaPostService") PostService jpaPostService,
            @Qualifier("jooqPostService") PostService jooqPostService) {
        this.jpaPostService = jpaPostService;
        this.jooqPostService = jooqPostService;
    }

    @GetMapping("/{user_name}/posts")
    public ResponseEntity<PostsDTO> getPostsByUserName(@PathVariable("user_name") String userName) {

        List<PostDTO> posts =
                this.jooqPostService.fetchAllPostsByUserName(userName).stream()
                        .map(postDTO -> this.addLinkToPostBiFunction.apply(userName, postDTO))
                        .collect(Collectors.toList());
        return ResponseEntity.of(Optional.of(new PostsDTO(posts)));
    }

    @GetMapping("/{user_name}/posts/{title}")
    public ResponseEntity<PostDTO> getPostByUserNameAndTitle(
            @PathVariable("user_name") String userName, @PathVariable("title") String title) {
        PostDTO postDTO =
                this.addLinkToPostBiFunction.apply(
                        userName,
                        this.jooqPostService.fetchPostByUserNameAndTitle(userName, title));
        Link getAllPostsLink =
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(this.getClass())
                                        .getPostsByUserName(userName))
                        .withRel("get-all-posts-by-username");
        postDTO.add(getAllPostsLink);

        return ResponseEntity.of(Optional.of(postDTO));
    }

    @PostMapping("/{user_name}/posts/")
    @Override
    public ResponseEntity<Object> createPostByUserName(
            @RequestBody @Valid PostRequestDTO postRequestDTO,
            @PathVariable("user_name") String userName,
            UriComponentsBuilder ucBuilder) {
        this.jpaPostService.createPost(postRequestDTO, userName);

        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("{title}")
                        .buildAndExpand(postRequestDTO.title())
                        .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{user_name}/posts/{title}")
    public ResponseEntity<PostDTO> updatePostByUserName(
            @RequestBody @Valid PostDTO postDTO,
            @PathVariable("user_name") String userName,
            @PathVariable("title") String title) {
        postDTO.setCreatedBy(userName);
        final PostDTO updatedPost = this.jpaPostService.updatePostByUserNameAndId(postDTO, title);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{user_name}/posts/{title}")
    public ResponseEntity<Void> deletePostByUserName(
            @PathVariable("user_name") String userName, @PathVariable("title") String title) {
        this.jpaPostService.deletePostByIdAndUserName(userName, title);
        return ResponseEntity.accepted().build();
    }
}
