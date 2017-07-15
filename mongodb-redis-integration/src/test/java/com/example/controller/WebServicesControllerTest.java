/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.model.Book;
import com.example.service.WebServicesService;

/**
 * @author rajakolli
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class WebServicesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WebServicesService service;

	private Book dummyBook;

	@Before
	public void setUp() throws IOException {
		dummyBook = new Book();
		dummyBook.setTitle("JUNIT_TITLE");
		dummyBook.setAuthor("JUNIT_AUTHOR");
		dummyBook.setId("JUNIT");
		dummyBook.setText("JUNIT_TEXT");
		dummyBook.setVersion(1);
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
						"{\"id\":\"JUNIT\",\"title\":\"JUNIT_TITLE\",\"author\":\"JUNIT_AUTHOR\",\"text\":\"JUNIT_TEXT\",\"version\":1}"));
	}

	@Test
	public void testDeleteCache() throws Exception {
		this.mockMvc
				.perform(get("/book/deleteCache")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}
}
