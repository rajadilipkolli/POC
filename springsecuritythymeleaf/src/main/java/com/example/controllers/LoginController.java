package com.example.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
    
    @GetMapping(value={"/login.html","/login"})
    public String login() {
        log.debug("Logging In");
      return "login";
    }

    // Login form with error
    @GetMapping(value={"/login-error.html","/login-error"})
    public String loginError(Model model) {
      model.addAttribute("loginError", true);
      return "login";
    }
    
    @GetMapping(value={"/error.html","/error"})
    public String error(HttpServletRequest request, Model model) {
      model.addAttribute("errorCode", request.getAttribute("javax.servlet.error.status_code"));
      Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
      String errorMessage = null;
      if (throwable != null) {
        errorMessage = throwable.getMessage();
      }
      model.addAttribute("errorMessage", errorMessage);
      return "error";
    }
}
