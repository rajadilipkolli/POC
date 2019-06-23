package org.poc.springboot.mongodb.security.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.poc.springboot.mongodb.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = { LoginController.class })
@WithMockUser
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomUserDetailsService userService;

	@Test
	void testLogin() throws Exception {
		fail("Not yet implemented");
	}

	@Test
	void testSignup() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateNewUser() {
		fail("Not yet implemented");
	}

	@Test
	void testDashboard() {
		fail("Not yet implemented");
	}

	@Test
	void testHome() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(view().name("home"));
	}

}
