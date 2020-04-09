/*
 * Copyright 2015-2018 the original author or authors.
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

import javax.validation.Valid;

import org.poc.springboot.mongodb.security.domain.User;
import org.poc.springboot.mongodb.security.service.CustomUserDetailsService;
import org.poc.springboot.mongodb.security.service.CustomUserDetailsServiceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC Login Controller.
 *
 * @author Raja Kolli
 * @since 0.2.1
 *
 */
@Controller
public class LoginController {

	private final CustomUserDetailsService userService;

	public LoginController(CustomUserDetailsServiceImpl userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping("/signup")
	public ModelAndView signup() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("signup");
		return modelAndView;
	}

	@PostMapping("/signup")
	public ModelAndView createNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Optional<User> userExists = this.userService.findUserByEmail(user.getEmail());
		if (userExists.isPresent()) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the username provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("signup");
		}
		else {
			this.userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("login");

		}
		return modelAndView;
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = this.userService.findUserByEmail(auth.getName());
		modelAndView.addObject("currentUser", user.get());
		modelAndView.addObject("fullName", "Welcome " + user.get().getFullname());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("dashboard");
		return modelAndView;
	}

	@GetMapping({ "/", "/home" })
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		return modelAndView;
	}

}
