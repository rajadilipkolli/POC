package com.example.poc.webmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;
import com.example.poc.webmvc.dto.PostCommentsDTO;
import com.example.poc.webmvc.dto.PostDTO;
import com.example.poc.webmvc.dto.PostsDTO;
import com.example.poc.webmvc.dto.TagDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@TestMethodOrder(MethodOrderer.MethodName.class)
class PostControllerITTest extends AbstractIntegrationTest {

    private PostDTO postDto;

    @BeforeAll
    void init() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.postDto = new PostDTO();
        this.postDto.setCreatedBy("junit");
        this.postDto.setTitle("PostTitle");
        this.postDto.setContent("post created By Junit");
        this.postDto.setCreatedOn(currentDateTime);
        this.postDto.setTags(Collections.singletonList(new TagDTO("junit")));
        this.postDto.setComments(Collections.singletonList(new PostCommentsDTO("junit Review")));
    }

    @Test
    void test01_FetchingPostsByUserId() {
        ResponseEntity<PostsDTO> response =
                userRestTemplate().getForEntity("/users/raja/posts", PostsDTO.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().postList()).isNotEmpty().hasSize(2);
        PostDTO postDTO = response.getBody().postList().get(0);
        assertThat(postDTO.getComments())
                .isNotEmpty()
                .hasSize(2)
                .contains(new PostCommentsDTO("Excellent"), new PostCommentsDTO("Good"));
        assertThat(postDTO.getTags())
                .isNotEmpty()
                .hasSize(2)
                .contains(new TagDTO("Java"), new TagDTO("Spring Boot"));
        assertThat(postDTO.getCreatedBy()).isEqualTo("raja");
        assertThat(postDTO.getContent()).isNull();
        assertThat(postDTO.getTitle()).isEqualTo("A Beautiful Post in Java");
    }

    @Test
    void test02_CreatingPost() {

        ResponseEntity<PostDTO> postResponse =
                userRestTemplate()
                        .postForEntity("/users/junit/posts/", this.postDto, PostDTO.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getContentLength()).isEqualTo(0);
        String location = postResponse.getHeaders().getFirst("Location");
        assertThat(location).contains("/users/junit/posts/" + this.postDto.getTitle());

        ResponseEntity<PostDTO> getResponse =
                userRestTemplate().getForEntity(location, PostDTO.class);
        assertThat(getResponse).isNotNull();
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        PostDTO postDTO = getResponse.getBody();
        assertThat(postDTO.getComments())
                .isNotEmpty()
                .hasSize(1)
                .contains(new PostCommentsDTO("junit Review"));
        assertThat(postDTO.getTags()).isNotEmpty().hasSize(1).contains(new TagDTO("junit"));
        assertThat(postDTO.getContent()).isEqualTo("post created By Junit");
        assertThat(postDTO.getCreatedBy()).isEqualTo("junit");
        assertThat(postDTO.getTitle()).isEqualTo("PostTitle");
    }

    @Test
    void test03_UpdatingPost() {
        this.postDto.setContent("Updating content using Junit");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<PostDTO> entity = new HttpEntity<>(this.postDto, headers);
        final ResponseEntity<PostDTO> response =
                userRestTemplate()
                        .exchange(
                                "/users/junit/posts/" + this.postDto.getTitle(),
                                HttpMethod.PUT,
                                entity,
                                PostDTO.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        PostDTO postDTO = response.getBody();
        assertThat(postDTO.getComments())
                .isNotEmpty()
                .hasSize(1)
                .contains(new PostCommentsDTO("junit Review"));
        assertThat(postDTO.getTags()).isNotEmpty().hasSize(1).contains(new TagDTO("junit"));
        assertThat(postDTO.getContent()).isEqualTo("Updating content using Junit");
        assertThat(postDTO.getCreatedBy()).isEqualTo("junit");
        assertThat(postDTO.getTitle()).isEqualTo("PostTitle");
    }

    @Test
    void test04_DeletingPost() {
        adminRestTemplate().delete("/users/junit/posts/" + this.postDto.getTitle());
        ResponseEntity<PostsDTO> response =
                userRestTemplate().getForEntity("/users/junit/posts", PostsDTO.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().postList()).isEmpty();
    }
}
