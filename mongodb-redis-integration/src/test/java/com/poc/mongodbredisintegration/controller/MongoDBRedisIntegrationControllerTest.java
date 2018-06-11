/*
 * Copyright 2015-2018 the original author or authors.
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

package com.poc.mongodbredisintegration.controller;

import com.poc.mongodbredisintegration.document.Book;
import com.poc.mongodbredisintegration.service.MongoDBRedisIntegrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Raja Kolli
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MongoDBRedisIntegrationController.class)
public class MongoDBRedisIntegrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MongoDBRedisIntegrationService service;

	private Book dummyBook;

	private MongoDBRedisIntegrationController controller;

	@BeforeEach
	public void setUp() throws Exception {
		this.controller = new MongoDBRedisIntegrationController(this.service);
		this.dummyBook = Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR")
				.bookId("JUNIT").text("JUNIT_TEXT").version(1).build();
	}

	@Test
	public void testSaveBook() throws Exception {
		given(this.service.save(this.dummyBook)).willReturn(this.dummyBook);
		this.mockMvc
				.perform(post("/book/saveBook").content(this.dummyBook.toString())
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateByTitle() throws Exception {
		given(this.service.updateByTitle("title", "author")).willReturn(this.dummyBook);
		this.mockMvc
				.perform(put("/book/updateByTitle/title/author")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().string("{\"bookId\":\"JUNIT\",\"title\":\"JUNIT_TITLE\","
						+ "\"author\":\"JUNIT_AUTHOR\",\"text\":\"JUNIT_TEXT\",\"version\":1}"));
	}

	@Test
	public void testDeleteBookByTitle() throws Exception {
		given(this.service.findBookByTitle("test")).willReturn(this.dummyBook);
		this.mockMvc
				.perform(delete("/book/deleteByTitle/test")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(
						content().string(containsString("Book with title test deleted")));
		given(this.service.findBookByTitle("test")).willReturn(null);
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
		given(this.service.count()).willReturn(3L);
		assertThat(this.controller.count()).isGreaterThan(0);
	}

	@Test
	public void testDeleteAll() {
		// TODO
	}

}
