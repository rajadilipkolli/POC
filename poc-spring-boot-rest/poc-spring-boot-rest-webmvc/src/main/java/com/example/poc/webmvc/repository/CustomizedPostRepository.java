package com.example.poc.webmvc.repository;

import com.poc.restfulpoc.entities.Post;
import java.util.Optional;

public interface CustomizedPostRepository {

    Optional<Post> findByDetailsCreatedByAndTitle(String userName, String title);
}
