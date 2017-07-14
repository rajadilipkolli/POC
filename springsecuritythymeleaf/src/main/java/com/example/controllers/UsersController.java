package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@AllArgsConstructor
public class UsersController {

    private final UserService userService;

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        log.debug("Getting users page");
        return new ModelAndView("users", "users", userService.getAllUsers());
    }


}
