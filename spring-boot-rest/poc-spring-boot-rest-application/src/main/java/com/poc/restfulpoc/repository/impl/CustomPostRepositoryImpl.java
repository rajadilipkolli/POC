package com.poc.restfulpoc.repository.impl;

import com.poc.restfulpoc.entities.Post;
import com.poc.restfulpoc.repository.CustomPostRepository;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository, JpaUtility {

	@PersistenceContext
	private EntityManager persistenceContextEntityManager;

	@Override
	@Transactional(readOnly = true)
	public Post findByDetailsCreatedByAndTitle(String userName, String title) {
		return doInJPA(entityManager -> {
			List<Post> _post = entityManager.createQuery("""
					SELECT p FROM Post p
					    LEFT JOIN FETCH p.comments
					    JOIN FETCH p.details d
					where d.createdBy = :user and p.title = :title
					""", Post.class).setParameter("user", userName).setParameter("title", title)
					.setHint(QueryHints.PASS_DISTINCT_THROUGH, false).getResultList();

			return entityManager.createQuery("""
					    SELECT distinct p FROM Post p
					        LEFT JOIN FETCH p.tags pt
					        LEFT JOIN FETCH pt.tag
					        JOIN p.details
					    where p in :posts
					""", Post.class).setParameter("posts", _post).setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
					.getSingleResult();
		}, persistenceContextEntityManager);
	}

}
