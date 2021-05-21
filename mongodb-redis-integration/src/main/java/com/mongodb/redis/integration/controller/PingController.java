package com.mongodb.redis.integration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping(value = "/")
    public String hello() {
        return "Hello World!";
    }
}
