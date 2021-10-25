package com.example.poc.webmvc.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.poc.webmvc.common.MockObjectCreator;
import com.example.poc.webmvc.service.PostService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired private MockMvc mvc;

    @MockBean
    @Qualifier("jpaPostService")
    private PostService jpaPostService;

    @MockBean
    @Qualifier("jooqPostService")
    private PostService jooqPostService;

    @Test
    void shouldReturnAllPostsWithLinks() throws Exception {
        given(this.jooqPostService.fetchAllPostsByUserName("junit"))
                .willReturn(List.of(MockObjectCreator.getPostDTO()));

        this.mvc
                .perform(get("/users/junit/posts").accept(MediaTypes.HAL_JSON_VALUE)) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) //
                .andExpect(jsonPath("$.postList[0].title", is("junitTitle"))) //
                .andExpect(jsonPath("$.postList[0].content", is("junitContent"))) //
                .andExpect(jsonPath("$.postList[0].createdBy", is("junit"))) //
                .andExpect(jsonPath("$.postList[0].createdOn", nullValue())) //
                .andExpect(jsonPath("$.postList[0].comments.size()", is(1))) //
                .andExpect(jsonPath("$.postList[0].comments[0].review", is("junitReview"))) //
                .andExpect(jsonPath("$.postList[0].tags.size()", is(1))) //
                .andExpect(jsonPath("$.postList[0].tags.[0].name", is("tag"))) //
                .andExpect(jsonPath("$.postList[0].links.size()", is(1))) //
                .andExpect(jsonPath("$.postList[0].links[0].rel", is("self"))) //
                .andExpect(
                        jsonPath(
                                "$.postList[0].links[0].href",
                                is("http://localhost/users/junit/posts/junitTitle")))
                .andReturn();
    }
}
