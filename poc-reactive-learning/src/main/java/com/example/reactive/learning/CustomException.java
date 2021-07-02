package com.example.reactive.learning;

public class CustomException extends Throwable {

    private static final long serialVersionUID = 8755264074226853036L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomException(Throwable e) {
        this.message = e.getMessage();
    }
}
