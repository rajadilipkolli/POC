/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.exception;

import java.io.Serial;
import lombok.Getter;

public class BookNotFoundException extends Exception {

    @Serial private static final long serialVersionUID = 1L;

    @Getter private final String message;

    public BookNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }
}
