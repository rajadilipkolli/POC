package com.example.quarkus;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Integration test for running in native mode
 * This is a standalone class that doesn't extend PostResourceTest to avoid @Inject issues
 */
@QuarkusIntegrationTest
public class PostResourceIT {
    
    @Test
    void getNoneExistedPost_shouldReturn404() {
        given()
            .when().get("/posts/nonexisted")
            .then()
            .statusCode(404);
    }
    
    @Test
    void createPost_shouldReturn201() {
        Map<String, String> post = new HashMap<>();
        post.put("title", "Test Post Title - IT");
        post.put("content", "Test Post Content - IT");
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201)
            .header("Location", notNullValue());
    }
    
    @Test
    void createAndGetPost_shouldWorkTogether() {
        // Create a post with a unique title
        Map<String, String> post = new HashMap<>();
        post.put("title", "Integration Test Post " + System.currentTimeMillis());
        post.put("content", "Testing the CRUD in native mode");
        
        // Get the location of the created post
        String location = given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201)
            .extract().header("Location");
            
        // Extract the ID from the location
        String id = location.substring(location.lastIndexOf("/") + 1);
        
        // Get the post by ID
        given()
            .when().get("/posts/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", is(post.get("title")))
            .body("content", is(post.get("content")))
            .body("id", is(id))
            .body("createdAt", notNullValue());
    }
    
    @Test
    void updatePost_shouldUpdate() {
        // First create a post with unique ID
        Map<String, String> post = new HashMap<>();
        String uniqueId = "UpdateTest-" + System.currentTimeMillis();
        post.put("title", "Original Title " + uniqueId);
        post.put("content", "Original Content " + uniqueId);
        
        String location = given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201)
            .extract().header("Location");
            
        String id = location.substring(location.lastIndexOf("/") + 1);
        
        // Now update the post
        Map<String, String> updatedPost = new HashMap<>();
        updatedPost.put("title", "Updated Title " + uniqueId);
        updatedPost.put("content", "Updated Content " + uniqueId);
        
        given()
            .contentType(ContentType.JSON)
            .body(updatedPost)
            .when().put("/posts/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", is(updatedPost.get("title")))
            .body("content", is(updatedPost.get("content")))
            .body("id", is(id));
            
        // Verify the update worked by getting the post
        given()
            .when().get("/posts/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", is(updatedPost.get("title")))
            .body("content", is(updatedPost.get("content")));
    }
    
    @Test
    void deletePost_shouldRemovePost() {
        // First create a post
        Map<String, String> post = new HashMap<>();
        post.put("title", "Delete Test Post " + System.currentTimeMillis());
        post.put("content", "This post will be deleted");
        
        String location = given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201)
            .extract().header("Location");
            
        String id = location.substring(location.lastIndexOf("/") + 1);
        
        // Delete the post
        given()
            .when().delete("/posts/" + id)
            .then()
            .statusCode(204); // No content response is typical for successful DELETE
            
        // Verify it's gone
        given()
            .when().get("/posts/" + id)
            .then()
            .statusCode(404); // Should return Not Found after deletion
    }
    
    @Test
    void getAllPosts_shouldReturnListOfPosts() {
        // Create a post to ensure there's something to retrieve
        Map<String, String> post = new HashMap<>();
        post.put("title", "Native Mode List Post - " + System.currentTimeMillis());
        post.put("content", "Testing listing in native mode");
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201);
        
        // Get all posts
        given()
            .when().get("/posts")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }
    
    @Test
    void updateNonExistentPost_shouldReturn404() {
        Map<String, String> updatedPost = new HashMap<>();
        updatedPost.put("title", "Updated Title");
        updatedPost.put("content", "Updated Content");
        
        given()
            .contentType(ContentType.JSON)
            .body(updatedPost)
            .when().put("/posts/nonexistent")
            .then()
            .statusCode(404);
    }
    
    @Test
    void deleteNonExistentPost_shouldReturn404() {
        given()
            .when().delete("/posts/nonexistent")
            .then()
            .statusCode(404);
    }
}
