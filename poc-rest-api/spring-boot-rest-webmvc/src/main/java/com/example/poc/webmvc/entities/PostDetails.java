package com.example.poc.webmvc.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "PostDetails")
@Table(name = "post_details")
@Getter
@Setter
public class PostDetails {

    @Id
    @SequenceGenerator(allocationSize = 5, name = "sequenceGenerator")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    private Long id;

    @Column(name = "created_on", updatable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    public PostDetails() {
        this.createdOn = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Post post;

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
        PostDetails other = (PostDetails) obj;
        return Objects.equals(this.createdBy, other.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.createdBy);
    }
}
