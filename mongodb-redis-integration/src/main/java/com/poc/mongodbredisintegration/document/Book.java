package com.poc.mongodbredisintegration.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Book implements Serializable {
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
