package org.mongodb.redis.integration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BookNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	@Getter
	private String message;

}
