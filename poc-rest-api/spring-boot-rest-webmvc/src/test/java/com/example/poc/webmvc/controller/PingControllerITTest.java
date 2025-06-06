/* Licensed under Apache-2.0 2025 */
package com.example.poc.webmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.poc.webmvc.common.AbstractIntegrationTest;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

class PingControllerITTest extends AbstractIntegrationTest {

    private static final String FRONTEND_ORIGIN = "http://localhost:4200";

    @Test
    void testPingEndpoint() {
        ResponseEntity<PingController.PingResponse> response =
                restTemplate().getForEntity("/api/ping", PingController.PingResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PingController.PingResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.message()).isEqualTo("Welcome");
    }

    @Test
    void testPingWithAuthentication() {
        ResponseEntity<PingController.PingResponse> response =
                restTemplate()
                        .getForEntity(
                                "/api/pingWithAuthentication", PingController.PingResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PingController.PingResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.message()).isEqualTo("Authentication successful");
    }

    @Test
    void testPingWithAuthenticationHeader() {
        // Create headers with Authorization
        HttpHeaders headers = new HttpHeaders();
        String auth = "testuser:password";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);

        ResponseEntity<PingController.PingResponse> response =
                restTemplate()
                        .exchange(
                                "/api/pingWithAuthentication",
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                PingController.PingResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PingController.PingResponse responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.message())
                .isEqualTo("Authentication successful for user: testuser");
    }

    @Test
    void testCorsHeadersForPingEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ORIGIN, FRONTEND_ORIGIN);
        headers.set(HttpHeaders.HOST, "localhost");

        ResponseEntity<PingController.PingResponse> response =
                restTemplate()
                        .exchange(
                                "/api/ping",
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                PingController.PingResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify CORS headers
        HttpHeaders responseHeaders = response.getHeaders();
        assertThat(responseHeaders.getAccessControlAllowOrigin()).isEqualTo(FRONTEND_ORIGIN);
        assertThat(responseHeaders.getAccessControlAllowCredentials()).isTrue();
    }

    @Test
    void testCorsPreflightRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ORIGIN, FRONTEND_ORIGIN);
        headers.set(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET");
        headers.set(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type,Authorization");

        ResponseEntity<Void> response =
                restTemplate()
                        .exchange(
                                "/api/ping",
                                HttpMethod.OPTIONS,
                                new HttpEntity<>(headers),
                                Void.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Verify CORS headers for preflight request
        HttpHeaders responseHeaders = response.getHeaders();
        assertThat(responseHeaders.getAccessControlAllowOrigin()).isEqualTo(FRONTEND_ORIGIN);

        String allowedMethodsHeader = responseHeaders.getFirst("Access-Control-Allow-Methods");
        assertThat(allowedMethodsHeader).isNotNull();
        assertThat(allowedMethodsHeader).contains("GET", "POST", "PUT", "DELETE", "OPTIONS");

        assertThat(responseHeaders.getAccessControlAllowCredentials()).isTrue();

        // Check allowed headers - should contain at least Content-Type and Authorization
        String allowedHeadersHeader = responseHeaders.getFirst("Access-Control-Allow-Headers");
        assertThat(allowedHeadersHeader).isNotNull();
        assertThat(allowedHeadersHeader).contains("Content-Type", "Authorization");
    }
}
