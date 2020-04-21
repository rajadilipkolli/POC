/*
 * Copyright 2015-2018 the original author or authors.
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
package com.poc.restfulpoc.data;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import com.poc.restfulpoc.entities.PostDetails;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class AllAssociationTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private EntityManager entityManager;

	@Test
	@Transactional
	void testAllAssociations() {
		Post post = new Post();
		post.setTitle("Postit");

		PostComment comment1 = new PostComment();
		comment1.setReview("Good");

		PostComment comment2 = new PostComment();
		comment2.setReview("Excellent");

		post.addComment(comment1);
		post.addComment(comment2);

		PostDetails postDetails = new PostDetails();
		postDetails.setCreatedBy("JUNIT");
		post.addDetails(postDetails);

		this.entityManager.persist(post);

		// 1. Use Criteria Builder to create a Criteria Query returning the
		// expected result object
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Post> query = builder.createQuery(Post.class);

		// 2. Define roots for tables which are involved in the query
		Root<Post> postRoot = query.from(Post.class);

		// 3. Define Predicates etc using Criteria Builder
		Predicate cond = builder.equal(postRoot.get("title"), "post");

		// 4. Add Predicates etc to the Criteria Query
		query.where(cond);

		// 5. Build the TypedQuery using the entity manager and criteria query
		TypedQuery<Post> q = this.entityManager.createQuery(query);
		List<Post> resultList = q.getResultList();

		assertThat(resultList).isEmpty();

		// Retrieving the data by joining and using EntityGraph

		CriteriaQuery<Post> query1 = builder.createQuery(Post.class);
		// 2. Define roots for tables which are involved in the query
		Root<Post> root1 = query1.from(Post.class);

		// 3. Define Predicates etc using Criteria Builder, comments should be available
		// in post entity
		Join<Post, PostComment> join = root1.join("comments", JoinType.LEFT);
		// 4. Add Predicates etc to the Criteria Query
		query1.select(root1).where(builder.equal(join.get("review"), "Excellent"));

		// 5. Creating EntityGraphs
		EntityGraph<Post> fetchGraph = this.entityManager.createEntityGraph(Post.class);
		fetchGraph.addSubgraph("comments");

		// 6. Build the TypedQuery using the entity manager and criteria query
		TypedQuery<Post> q1 = this.entityManager.createQuery(query1).setHint("javax.persistence.loadgraph", fetchGraph);
		resultList = q1.getResultList();
		assertThat(resultList).isNotEmpty().size().isGreaterThanOrEqualTo(1);

	}

}
