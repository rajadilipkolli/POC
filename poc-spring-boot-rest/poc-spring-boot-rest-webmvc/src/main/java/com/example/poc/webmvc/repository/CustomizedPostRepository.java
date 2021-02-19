package com.example.poc.webmvc.repository;

import com.poc.restfulpoc.entities.Post;

public interface CustomizedPostRepository {

    Post findByDetailsCreatedByAndTitle(String userName, String title);
}
