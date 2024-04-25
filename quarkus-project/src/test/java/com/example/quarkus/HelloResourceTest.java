package com.example.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class HelloResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

    @Test
    void testGreetEndpoint() {
        given().queryParam("name", "raja")
                .when().get("/hello/greet")
                .then()
                .statusCode(200)
                .body(is("Hello raja"));
    }

    @Test
    void testGreetingEndpoint() {
        given().pathParam("name", "raja")
                .when().get("/hello/polite/{name}")
                .then()
                .statusCode(200)
                .body(is("Good morning raja"));
    }

}