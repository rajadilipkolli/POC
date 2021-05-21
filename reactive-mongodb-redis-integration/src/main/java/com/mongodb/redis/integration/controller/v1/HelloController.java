package com.mongodb.redis.integration.controller.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @RequestMapping(value = "/")
  String hello() {
    return "Hello World!";
  }
}
