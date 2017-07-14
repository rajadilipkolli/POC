package com.example.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

@Data
public class Book implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @Indexed
    private String title;
    private String author;
    private String text;
    @Version
    private int version;
}
