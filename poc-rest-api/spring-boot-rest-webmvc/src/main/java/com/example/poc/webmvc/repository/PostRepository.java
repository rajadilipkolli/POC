/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.repository;

import com.example.poc.webmvc.dto.PostCommentProjection;
import com.example.poc.webmvc.entities.Post;
import jakarta.persistence.QueryHint;
import java.util.List;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomizedPostRepository {

    @Query(
            "SELECT p.title as title, p.content as content, c.review as review FROM Post p JOIN p.comments c where p.title = :title")
    @Transactional(readOnly = true)
    List<PostCommentProjection> findByTitle(@Param("title") String title);

    @Query("SELECT p.id FROM Post p where p.title Like :title")
    @Transactional(readOnly = true)
    List<Long> findByTitleContaining(@Param("title") String title);

    @Query(
            "SELECT p.title as title, c.review as review FROM Post p JOIN p.comments c where p.id IN :ids")
    @Transactional(readOnly = true)
    List<PostCommentProjection> findByIds(@Param("ids") List<Long> items);

    @Query(
            "SELECT distinct p FROM Post p LEFT JOIN FETCH p.comments JOIN FETCH p.details d where d.createdBy = :user")
    @QueryHints({@QueryHint(name = AvailableHints.HINT_READ_ONLY, value = "true")})
    List<Post> findByDetailsCreatedBy(@Param("user") String userName);

    @Query(
            "SELECT distinct p FROM Post p LEFT JOIN FETCH p.tags pt LEFT JOIN FETCH pt.tag JOIN p.details where p in :posts")
    @QueryHints({@QueryHint(name = AvailableHints.HINT_READ_ONLY, value = "true")})
    List<Post> findPostsWithAllDetails(@Param("posts") List<Post> postList);

    @Modifying
    void deleteByTitleAndDetailsCreatedBy(String title, String username);
}
