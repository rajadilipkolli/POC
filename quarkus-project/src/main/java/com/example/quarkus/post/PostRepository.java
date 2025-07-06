package com.example.quarkus.post;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PostRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * Remove all posts from the database. Used for test isolation.
     */
    @Transactional
    public void clearAll() {
        em.createQuery("DELETE FROM Post").executeUpdate();
    }

    @Transactional
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    public Post getById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> getAllPosts() {
        return em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Post post = em.find(Post.class, id);
        if (post != null) {
            em.remove(post);
        }
    }

    @Transactional
    public Post update(Post post) {
        Post existingPost = em.find(Post.class, post.getId());
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            return em.merge(existingPost);
        }
        return null;
    }
}
