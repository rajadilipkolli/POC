package com.example;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Book implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    private String title;
    private String author;
    private String text;
}
