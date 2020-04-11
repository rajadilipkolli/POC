/*
 * Copyright 2015-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poc.restfulpoc.repository;

import java.util.List;

import javax.persistence.QueryHint;

import com.poc.restfulpoc.dto.PostCommentProjection;
import com.poc.restfulpoc.entities.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p.title as title, c.review as review FROM Post p JOIN p.comments c where p.title = :title")
	@Transactional(readOnly = true)
	List<PostCommentProjection> findByTitle(@Param("title") String title);

	@Query("SELECT p.id FROM Post p where p.title Like :title")
	@Transactional(readOnly = true)
	List<Long> findByTitleContaining(@Param("title") String title);

	@Query("SELECT p.title as title, c.review as review FROM Post p JOIN p.comments c where p.id IN :ids")
	@Transactional(readOnly = true)
	List<PostCommentProjection> findByIds(@Param("ids") List<Long> items);

	@Query("SELECT distinct p FROM Post p JOIN FETCH p.comments JOIN FETCH p.details d where d.createdBy = :user")
	@QueryHints(@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value = "false"))
	List<Post> findByDetailsCreatedBy(@Param("user") String userName);

	@Query("SELECT distinct p FROM Post p JOIN FETCH p.tags JOIN p.details where p in :posts")
	@QueryHints(@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH, value = "false"))
	List<Post> findPostsWithAllDetails(@Param("posts") List<Post> postList);

}
