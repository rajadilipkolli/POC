package com.example.poc.webmvc.repository;

import com.poc.restfulpoc.entities.PostTag;
import com.poc.restfulpoc.entities.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {}
