/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity(name = "Post")
@Table(name = "post")
@Getter
@Setter
public class Post {

    @Id
    @SequenceGenerator(allocationSize = 5, name = "sequenceGenerator")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    private Long id;

    private String title;

    @Column(length = 4096)
    private String content;

    @LastModifiedDate private LocalDateTime updatedOn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    @OneToOne(
            cascade = CascadeType.ALL,
            mappedBy = "post",
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private PostDetails details;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> tags = new ArrayList<>();

    public Post() {
        this.updatedOn = LocalDateTime.now();
    }

    public Post(Long id) {
        this.id = id;
        this.updatedOn = LocalDateTime.now();
    }

    public Post(String title) {
        this.title = title;
        this.updatedOn = LocalDateTime.now();
    }

    public void addComment(PostComment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostComment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }

    public void addDetails(PostDetails details) {
        this.details = details;
        details.setPost(this);
    }

    public void removeDetails() {
        this.details.setPost(null);
        this.details = null;
    }

    public void addTag(Tag tag) {
        PostTag postTag = new PostTag(this, tag);
        this.tags.add(postTag);
    }

    public void removeTag(Tag tag) {
        for (Iterator<PostTag> iterator = this.tags.iterator(); iterator.hasNext(); ) {
            PostTag postTag = iterator.next();

            if (postTag.getPost().equals(this) && postTag.getTag().equals(tag)) {
                iterator.remove();
                postTag.setPost(null);
                postTag.setTag(null);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Post other = (Post) obj;
        return Objects.equals(this.details, other.details)
                && Objects.equals(this.title, other.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.details, this.title);
    }
}
