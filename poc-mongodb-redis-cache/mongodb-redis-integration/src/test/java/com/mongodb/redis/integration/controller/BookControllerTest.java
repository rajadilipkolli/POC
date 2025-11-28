/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private BookService bookService;

    @Test
    void getBookShouldReturnBook() throws Exception {
        given(this.bookService.findBookByTitle(anyString()))
                .willReturn(
                        new Book()
                                .setTitle("JUNIT_TITLE")
                                .setAuthor("JUNIT_AUTHOR")
                                .setBookId("JUNIT")
                                .setText("JUNIT_TEXT")
                                .setVersion(1L));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/book/findByTitle/JUNIT_TITLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("JUNIT_TITLE"))
                .andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
    }

    @Test
    void getBookNotFound() throws Exception {
        given(this.bookService.findBookByTitle(anyString()))
                .willThrow(new BookNotFoundException("Book with Title Not Found"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/book/findByTitle/MongodbBook"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBookShouldReturnBook() throws Exception {
        Book book =
                new Book()
                        .setTitle("JUNIT_TITLE")
                        .setAuthor("JUNIT_AUTHOR")
                        .setBookId("JUNIT")
                        .setText("JUNIT_TEXT")
                        .setVersion(1L);
        given(this.bookService.saveBook(ArgumentMatchers.any(Book.class))).willReturn(book);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/book/saveBook")
                                .content(this.objectMapper.writeValueAsString(book))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("JUNIT_TITLE"))
                .andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
    }

    @Test
    void updateBookShouldReturnBook() throws Exception {
        Book book =
                new Book()
                        .setTitle("MongoDbCookBook")
                        .setAuthor("JUNIT_AUTHOR")
                        .setBookId("JUNIT")
                        .setText("JUNIT_TEXT")
                        .setVersion(1L);
        given(
                        this.bookService.updateAuthorByTitle(
                                ArgumentMatchers.eq("MongoDbCookBook"), anyString()))
                .willReturn(book);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/book/updateByTitle/MongoDbCookBook/Raja1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("MongoDbCookBook"))
                .andExpect(jsonPath("author").value("JUNIT_AUTHOR"));
    }

    @Test
    void deleteBookShouldReturnError() throws Exception {
        willThrow(new BookNotFoundException("Book with Title Not Found"))
                .given(this.bookService)
                .deleteBook(anyString());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/book/deleteByTitle/MongoDbCookBook"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBookShouldReturnValue() throws Exception {
        willDoNothing().given(this.bookService).deleteBook(anyString());

        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/book/deleteByTitle/MongoDbCookBook"))
                .andExpect(status().isAccepted());
    }
}
