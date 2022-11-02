package com.example.poc.webmvc.exception;

public class PostNotFoundException extends RuntimeException {

    private final String message;

    public PostNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
