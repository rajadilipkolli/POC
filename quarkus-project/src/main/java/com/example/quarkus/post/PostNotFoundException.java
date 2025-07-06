package com.example.quarkus.post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Post:" + id + " was not found!");
    }
}
