/* Licensed under Apache-2.0 2021-2022 */
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
