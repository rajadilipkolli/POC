package com.poc.restfulpoc.repository;

import com.poc.restfulpoc.entities.Post;

public interface CustomPostRepository {

	Post findByDetailsCreatedByAndTitle(String userName, String title);

}
