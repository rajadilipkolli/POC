package com.example.poc.reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("reactive_posts")
public class ReactivePost {

    @Id
    @Column("id")
    private Integer id;

    @Column("title")
    private String title;

    @Column("content")
    private String content;

    public ReactivePost(String title, String content) {
        this.id = null;
        this.title = title;
        this.content = content;
    }
}
