package com.example.quarkus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.quarkus.post.PostRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
public class PostResourceTest {

    @Inject
    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        // Clear the repository data before each test to ensure isolation
        postRepository.clearAll();
    }

    @Test
    void getNoneExistedPostShouldReturn404() {
        given()
            .when().get("/posts/999999") // Use a non-existent numeric ID
            .then()
            .statusCode(404);
    }
    
    @Test
    void createPostShouldReturn201() {
        // Create a post using POST request
        Map<String, String> post = new HashMap<>();
        post.put("title", "Test Post Title");
        post.put("content", "Test Post Content");
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(201)
            .header("Location", notNullValue());
    }
    
    @Test
    void createAndGetPostShouldWorkTogether() {
        // Create a post
        Map<String, String> post = new HashMap<>();
        post.put("title", "Integration Test Post");
        post.put("content", "Testing the full CRUD cycle");
        
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
            .body("title", is("Integration Test Post"))
            .body("content", is("Testing the full CRUD cycle"))
            .body("id", is(Integer.parseInt(id)))
            .body("createdAt", notNullValue());
    }
    
    @Test
    void postWithoutRequiredFieldsShouldFail() {
        // Create invalid post missing required fields
        Map<String, String> post = new HashMap<>();
        post.put("title", ""); // Empty title
        
        given()
            .contentType(ContentType.JSON)
            .body(post)
            .when().post("/posts/")
            .then()
            .statusCode(400); // Assuming validation returns 400 Bad Request
    }

    @Test
    void updatePostShouldUpdate() {
        // First create a post
        Map<String, String> post = new HashMap<>();
        post.put("title", "Original Title");
        post.put("content", "Original Content");
        
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
        updatedPost.put("title", "Updated Title");
        updatedPost.put("content", "Updated Content");
        
        given()
            .contentType(ContentType.JSON)
            .body(updatedPost)
            .when().put("/posts/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", is("Updated Title"))
            .body("content", is("Updated Content"))
            .body("id", is(Integer.parseInt(id)));
            
        // Verify the update worked by getting the post
        given()
            .when().get("/posts/" + id)
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("title", is("Updated Title"))
            .body("content", is("Updated Content"));
    }
    
    @Test
    void deletePostShouldRemovePost() {
        // First create a post
        Map<String, String> post = new HashMap<>();
        post.put("title", "Delete Test Post");
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
    void getAllPostsShouldReturnListOfPosts() {
        // Create multiple posts
        for (int i = 1; i <= 3; i++) {
            Map<String, String> post = new HashMap<>();
            post.put("title", "List Post " + i);
            post.put("content", "Content for post " + i);
            
            given()
                .contentType(ContentType.JSON)
                .body(post)
                .when().post("/posts/")
                .then()
                .statusCode(201);
        }
        
        // Get all posts
        given()
            .when().get("/posts")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", is(3));  // Assuming at least 3 posts exist
    }
    
    @Test
    void updateNonExistentPostShouldReturn404() {
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
    void deleteNonExistentPostShouldReturn404() {
        given()
            .when().delete("/posts/nonexistent")
            .then()
            .statusCode(404);
    }
}
