/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PingController {

    /**
     * Endpoint to check if the service is running.
     *
     * @return a simple "pong" response.
     */
    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    PingResponse ping(@RequestParam(required = false) String userName) {
        if (userName == null) {
            return new PingResponse("Welcome");
        } else {
            return new PingResponse("Welcome " + userName);
        }
    }

    record PingResponse(String message) {}
}
