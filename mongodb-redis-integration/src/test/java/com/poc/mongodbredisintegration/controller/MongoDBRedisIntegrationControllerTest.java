/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.service.MongoDBRedisIntegrationService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MongoDBRedisIntegrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MongoDBRedisIntegrationService service;

    private Book dummyBook;

    private MongoDBRedisIntegrationController controller;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new MongoDBRedisIntegrationController(service);
        dummyBook = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR").id("JUNIT")
                .text("JUNIT_TEXT").version(1).build();
    }

    @Test
    public void testSaveBook() throws Exception {
        when(service.save(dummyBook)).thenReturn(dummyBook);
        this.mockMvc
                .perform(post("/book/saveBook").content(dummyBook.toString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateByTitle() throws Exception {
        when(service.updateByTitle("title", "author")).thenReturn(dummyBook);
        this.mockMvc
                .perform(put("/book/updateByTitle/title/author")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content().string(
                        "{\"id\":\"JUNIT\",\"title\":\"JUNIT_TITLE\","
                        + "\"author\":\"JUNIT_AUTHOR\",\"text\":\"JUNIT_TEXT\",\"version\":1}"));
    }

    @Test
    public void testDeleteBookByTitle() throws Exception {
        when(service.findBookByTitle("test")).thenReturn(dummyBook);
        this.mockMvc
                .perform(delete("/book/deleteByTitle/test")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(
                        content().string(containsString("Book with title test deleted")));
        when(service.findBookByTitle("test")).thenReturn(null);
        this.mockMvc
                .perform(delete("/book/deleteByTitle/test")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(content()
                        .string(containsString("Book with title test Not Found")));
    }

    @Test
    public void testDeleteCache() throws Exception {
        this.mockMvc
                .perform(get("/book/deleteCache")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testCount() {
        when(service.count()).thenReturn(3L);
        assertThat(controller.count()).isGreaterThan(0);
    }

    @Test
    public void testDeleteAll() {
        //TODO
    }

}
