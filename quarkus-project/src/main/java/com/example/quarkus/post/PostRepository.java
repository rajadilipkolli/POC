package com.example.quarkus.post;

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

}
