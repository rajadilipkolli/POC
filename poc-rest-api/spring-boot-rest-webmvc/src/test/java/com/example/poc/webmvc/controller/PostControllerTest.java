/* Licensed under Apache-2.0 2021-2023 */
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
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired private MockMvc mvc;

    @MockitoBean
    @Qualifier("jpaPostService")
    private PostService jpaPostService;

    @MockitoBean
    @Qualifier("jooqPostService")
    private PostService jooqPostService;

    @Test
    void shouldReturnAllPostsWithLinks() throws Exception {
        given(this.jooqPostService.fetchAllPostsByUserName("junit"))
                .willReturn(List.of(MockObjectCreator.getPostDTO()));

        this.mvc
                .perform(get("/api/users/junit/posts").accept(MediaTypes.HAL_JSON_VALUE)) //
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
                                is("http://localhost/api/users/junit/posts/junitTitle")))
                .andReturn();
    }
}
