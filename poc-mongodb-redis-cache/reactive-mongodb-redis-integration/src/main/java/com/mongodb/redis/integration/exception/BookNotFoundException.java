/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.exception;

import lombok.Getter;

import java.io.Serial;

public class BookNotFoundException extends Exception {

    @Serial private static final long serialVersionUID = 1L;

    @Getter private final String message;

    public BookNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }
}
