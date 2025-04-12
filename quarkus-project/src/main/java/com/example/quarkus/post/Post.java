package com.example.quarkus.post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Post implements Serializable {
    String id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    String title;
    
    @NotBlank(message = "Content is required")
    String content;
    
    LocalDateTime createdAt;
    
    public static Post of(String title, String content) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setCreatedAt(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);
        return post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
