/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.poc.springboot.mongodb.security.controller;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.poc.springboot.mongodb.security.config.CustomizeAuthenticationSuccessHandler;
import org.poc.springboot.mongodb.security.service.CustomUserDetailsServiceImpl;
import org.poc.springboot.mongodb.security.util.MockObjectCreator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = { LoginController.class })
@WithMockUser
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomUserDetailsServiceImpl userService;

	@MockBean
	CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

	@Test
	void testLogin() throws Exception {
		this.mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	void testSignup() throws Exception {
		this.mockMvc.perform(get("/signup")).andExpect(status().isOk()).andExpect(view().name("signup"))
				.andExpect(model().attributeExists("user")).andExpect(model().errorCount(0))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	void testCreateNewUser() throws Exception {

		given(this.userService.findUserByEmail("junit@email.com")).willReturn(Optional.empty());
		willDoNothing().given(this.userService).saveUser(MockObjectCreator.getUser());

		this.mockMvc
				.perform(post("/signup").content(asJsonString(MockObjectCreator.getUser()))
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(view().name("login")).andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("successMessage", "User has been registered successfully"))
				.andExpect(model().errorCount(0)).andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	void testCreateNewUser_whenUserExists() throws Exception {
		given(this.userService.findUserByEmail("junit@email.com")).willReturn(Optional.of(MockObjectCreator.getUser()));

		this.mockMvc
				.perform(post("/signup").content(asJsonString(MockObjectCreator.getUser()))
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(view().name("signup")).andExpect(model().errorCount(1));
	}

	@Test
	@WithMockUser(authorities = "ADMIN", username = "junitAdmin@email.com")
	void testDashboard() throws Exception {
		given(this.userService.findUserByEmail("junitAdmin@email.com"))
				.willReturn(Optional.of(MockObjectCreator.getUser()));
		this.mockMvc.perform(get("/dashboard")).andExpect(status().isOk()).andExpect(view().name("dashboard"))
				.andExpect(model().attributeExists("currentUser"))
				.andExpect(model().attribute("fullName", "Welcome junitFullName"))
				.andExpect(model().attribute("adminMessage", "Content Available Only for Users with Admin Role"))
				.andExpect(model().errorCount(0)).andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	@Test
	void testDashboard_notAuthorized() throws Exception {
		this.mockMvc.perform(get("/dashboard")).andExpect(status().isForbidden());
	}

	@Test
	void testHome() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("home"))
				.andExpect(content().contentType("text/html;charset=UTF-8"));
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
