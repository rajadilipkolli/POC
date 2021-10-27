package com.example.poc.reactive.exception;

import lombok.Getter;

public class PostNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter private final String message;

    public PostNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }
}
