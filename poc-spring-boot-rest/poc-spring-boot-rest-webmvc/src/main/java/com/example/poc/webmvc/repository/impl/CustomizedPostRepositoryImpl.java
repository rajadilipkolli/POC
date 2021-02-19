package com.example.poc.webmvc.repository.impl;

import com.example.poc.webmvc.repository.CustomizedPostRepository;
import com.poc.restfulpoc.entities.Post;
import java.util.List;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomizedPostRepositoryImpl extends JpaUtility implements CustomizedPostRepository {

    @Override
    @Transactional(readOnly = true)
    public Post findByDetailsCreatedByAndTitle(String userName, String title) {

        return doInJPA(
                entityManager -> {
                    List<Post> _post =
                            entityManager
                                    .createQuery(
                                            "SELECT p FROM Post p LEFT JOIN FETCH p.comments JOIN FETCH p.details d where d.createdBy = :user and p.title = :title ",
                                            Post.class)
                                    .setParameter("user", userName)
                                    .setParameter("title", title)
                                    .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                                    .setHint(QueryHints.READ_ONLY, true)
                                    .getResultList();

                    return entityManager
                            .createQuery(
                                    "SELECT distinct p FROM Post p LEFT JOIN FETCH p.tags pt LEFT JOIN FETCH pt.tag JOIN p.details where p in :posts",
                                    Post.class)
                            .setParameter("posts", _post)
                            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                            .setHint(QueryHints.READ_ONLY, true)
                            .getSingleResult();
                });
    }
}
