/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.repository.impl;

import com.example.poc.webmvc.entities.Post;
import com.example.poc.webmvc.repository.CustomizedPostRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomizedPostRepositoryImpl extends JpaUtility implements CustomizedPostRepository {

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> findByDetailsCreatedByAndTitle(String userName, String title) {

        return doInJPA(
                entityManager -> {
                    List<Post> postList =
                            entityManager
                                    .createQuery(
                                            """
                                                    SELECT
                                                              p
                                                    FROM
                                                              Post p
                                                              LEFT JOIN
                                                    FETCH p.comments
                                                          JOIN
                                                    FETCH p.details d
                                                    where
                                                          d.createdBy = :user
                                                          and p.title = :title
                                                    """,
                                            Post.class)
                                    .setParameter("user", userName)
                                    .setParameter("title", title)
                                    //
                                    // .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                                    .setHint(AvailableHints.HINT_READ_ONLY, true)
                                    .getResultList();
                    if (!postList.isEmpty()) {
                        return Optional.of(
                                entityManager
                                        .createQuery(
                                                """
                                                        SELECT distinct
                                                                  p
                                                        FROM
                                                                  Post p
                                                                  LEFT JOIN
                                                        FETCH     p.tags pt
                                                                  LEFT JOIN
                                                        FETCH pt.tag
                                                              JOIN
                                                                    p.details
                                                        where
                                                              p in :posts
                                                        """,
                                                Post.class)
                                        .setParameter("posts", postList)
                                        //
                                        // .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                                        .setHint(AvailableHints.HINT_READ_ONLY, true)
                                        .getSingleResult());
                    }
                    return Optional.empty();
                });
    }
}
