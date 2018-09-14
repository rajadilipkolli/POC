package org.mongodb.redis.integration.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private String message;

	public BookNotFoundException(String message) {
		this.message = message;
	}

}
