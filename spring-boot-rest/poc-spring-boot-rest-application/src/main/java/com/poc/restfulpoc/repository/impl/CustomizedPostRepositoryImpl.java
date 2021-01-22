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

package com.poc.restfulpoc.repository.impl;

import java.util.List;

import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.repository.CustomizedPostRepository;
import org.hibernate.annotations.QueryHints;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomizedPostRepositoryImpl extends JpaUtility implements CustomizedPostRepository {

	@Override
	@Transactional(readOnly = true)
	public Post findByDetailsCreatedByAndTitle(String userName, String title) {

		return doInJPA(entityManager -> {
			List<Post> _post = entityManager.createQuery(
					"SELECT p FROM Post p LEFT JOIN FETCH p.comments JOIN FETCH p.details d where d.createdBy = :user and p.title = :title ",
					Post.class).setParameter("user", userName).setParameter("title", title)
					.setHint(QueryHints.PASS_DISTINCT_THROUGH, false).getResultList();

			return entityManager.createQuery(
					"SELECT distinct p FROM Post p LEFT JOIN FETCH p.tags pt LEFT JOIN FETCH pt.tag JOIN p.details where p in :posts",
					Post.class).setParameter("posts", _post).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
					.getSingleResult();
		});
	}

}
