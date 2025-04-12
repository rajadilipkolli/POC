package com.example.quarkus.post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository {

    static Map<String, Post> data = new ConcurrentHashMap<>();

    public Post save(Post post) {
        data.put(post.getId(), post);
        return post;
    }

    public Post getById(String id) {
       return data.get(id);
    }
    
    public Collection<Post> getAllPosts() {
        return data.values();
    }

    public void deleteById(String id) {
        data.remove(id);
    }

    public Post update(Post post) {
        Post existingPost = data.get(post.getId());
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            return existingPost;
        }
        return null;
    }
    
    // Method to clear all posts - useful for testing
    public void clearAll() {
        data.clear();
    }
}
