package com.example.quarkus;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class PostResourceTest {

    @Test
    void getNoneExistedPost_shouldReturn404() {
    given()
        .when().get("/posts/nonexisted")
        .then()
        .statusCode(404);
}
}
