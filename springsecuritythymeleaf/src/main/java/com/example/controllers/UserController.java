package com.example.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.UserCreateForm;
import com.example.domain.validator.UserCreateFormValidator;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserCreateFormValidator userCreateFormValidator;

	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	@RequestMapping("/user/{id}")
	@Secured(value = { "ROLE_USER" })
	public ModelAndView getUserPage(@PathVariable String id) {
		log.debug("Getting user page for user={}", id);
		return new ModelAndView("user", "user",
				userService.getUserById(id).orElseThrow(() -> new NoSuchElementException(
						String.format("User=%s not found", id))));
	}

	@RequestMapping(value = "/user/create", method = RequestMethod.GET)
	public ModelAndView getUserCreatePage() {
		log.debug("Getting user create form");
		return new ModelAndView("user_create", "form", new UserCreateForm());
	}

	// @PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(value = "/user/create")
	public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form,
			BindingResult bindingResult) {
		log.debug("Processing user create form={}, bindingResult={}", form,
				bindingResult);
		if (bindingResult.hasErrors()) {
			// failed validation
			return "user_create";
		}
		try {
			userService.create(form);
		}
		catch (DataIntegrityViolationException e) {
			// probably email already exists - very rare case when multiple admins are
			// adding same user
			// at the same time and form validation has passed for more than one of them.
			log.warn(
					"Exception occurred when trying to save the user, assuming duplicate email",
					e);
			bindingResult.reject("email.exists", "Email already exists");
			return "user_create";
		}
		// ok, redirect
		return "redirect:/login";
	}

}
