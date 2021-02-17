package com.mongodb.redis.integration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  @RequestMapping(value = "/")
  String hello() {
    return "Hello World!";
  }
}
