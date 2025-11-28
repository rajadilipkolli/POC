/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.exception;

public class BookNotFoundException extends Exception {

    private final String message;

    public BookNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
        this.message = exceptionMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
