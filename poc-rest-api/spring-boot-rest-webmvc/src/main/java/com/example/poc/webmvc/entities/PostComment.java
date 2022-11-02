package com.example.poc.webmvc.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "PostComment")
@Table(name = "post_comment")
@Getter
@Setter
public class PostComment {

    @Id
    @SequenceGenerator(allocationSize = 5, name = "sequenceGenerator")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    private Long id;

    private String review;

    private LocalDateTime createdOn;

    public PostComment() {
        this.createdOn = LocalDateTime.now();
    }

    public PostComment(String review) {
        this.review = review;
        this.createdOn = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.review != null && this.review.equals(((PostComment) o).getReview());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
