/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.controller;

import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Endpoint for authentication testing. This endpoint is used by the frontend for basic
     * authentication validation. It extracts the username from the Authorization header if present.
     *
     * @param authorizationHeader the Authorization header containing Basic auth credentials
     * @return a response indicating successful authentication, including the username if available
     */
    @GetMapping(value = "/pingWithAuthentication", produces = MediaType.APPLICATION_JSON_VALUE)
    PingResponse pingWithAuthentication(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String userName = extractUserNameFromAuthHeader(authorizationHeader);
        if (userName != null) {
            return new PingResponse("Authentication successful for user: " + userName);
        } else {
            return new PingResponse("Authentication successful");
        }
    }

    /**
     * Extract username from Basic Authentication header.
     *
     * @param authHeader the Authorization header value
     * @return the extracted username or null if not possible
     */
    private String extractUserNameFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return null;
        }

        try {
            // Remove "Basic " prefix and decode
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            // credentials = username:password
            return credentials.split(":", 2)[0];
        } catch (Exception e) {
            return null;
        }
    }

    record PingResponse(String message) {}
}
