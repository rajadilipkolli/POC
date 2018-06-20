/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.data;

import java.util.List;

import javax.persistence.EntityManager;

import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.entities.PostComment;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AllAssociationTest {

	@Autowired
	EntityManager entityManager;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	void test() {
		Post post = new Post(1L);
		post.setTitle("Postit");

		PostComment comment1 = new PostComment();
		comment1.setId(1L);
		comment1.setReview("Good");

		PostComment comment2 = new PostComment();
		comment2.setId(2L);
		comment2.setReview("Excellent");

		post.addComment(comment1);
		post.addComment(comment2);
		this.entityManager.persist(post);

		Session session = this.entityManager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Post.class)
				.add(Restrictions.eq("title", "post"));
		assertThat(criteria.list()).isEmpty();

		Session session1 = this.entityManager.unwrap(Session.class);
		List<Post> posts = session1.createCriteria(Post.class)
				.setFetchMode("comments", FetchMode.JOIN)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("title", "Postit")).list();
		assertThat(posts).isNotEmpty().size().isEqualTo(1);

		Session session11 = this.entityManager.unwrap(Session.class);
		List<Post> posts1 = session11.createCriteria(Post.class, "post")
				.setFetchMode("post.comments", FetchMode.JOIN)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("post.title", "Postit")).list();
		assertThat(posts1).isNotEmpty().size().isEqualTo(1);
	}

}
