/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.repository;

import com.example.poc.webmvc.entities.PostComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {}
