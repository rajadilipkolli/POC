/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.exception;

import java.io.Serial;

public class PostNotFoundException extends Exception {

    @Serial private static final long serialVersionUID = 1L;

    private final String message;

    public PostNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
