/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.engine.path.PathImpl;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * <p>
 * ApiError class.
 * </p>
 *
 * @author Raja Kolli
 * @version 1: 0
 */
@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM, property = "error",
		visible = true)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
public class ApiError {

	private HttpStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	private String message;

	private String debugMessage;

	private List<ApiSubError> subErrors;

	private ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	/**
	 * <p>
	 * Constructor for ApiError.
	 * </p>
	 * @param status a {@link org.springframework.http.HttpStatus} object.
	 */
	public ApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	/**
	 * <p>
	 * Constructor for ApiError.
	 * </p>
	 * @param status a {@link org.springframework.http.HttpStatus} object.
	 * @param ex a {@link java.lang.Throwable} object.
	 */
	public ApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "Unexpected error";
		this.debugMessage = ex.getLocalizedMessage();
	}

	/**
	 * <p>
	 * Constructor for ApiError.
	 * </p>
	 * @param status a {@link org.springframework.http.HttpStatus} object.
	 * @param message a {@link java.lang.String} object.
	 * @param ex a {@link java.lang.Throwable} object.
	 */
	public ApiError(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	private void addSubError(ApiSubError subError) {
		if (this.subErrors == null) {
			this.subErrors = new ArrayList<>();
		}
		this.subErrors.add(subError);
	}

	private void addValidationError(String object, String field, Object rejectedValue, String message) {
		addSubError(new ApiValidationError(object, field, rejectedValue, message));
	}

	private void addValidationError(String object, String message) {
		addSubError(new ApiValidationError(object, message));
	}

	private void addValidationError(FieldError fieldError) {
		this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

	/**
	 * <p>
	 * addValidationErrors.
	 * </p>
	 * @param fieldErrors a {@link java.util.List} object.
	 */
	public void addValidationErrors(List<FieldError> fieldErrors) {
		fieldErrors.forEach(this::addValidationError);
	}

	private void addValidationError(ObjectError objectError) {
		this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
	}

	/**
	 * <p>
	 * addValidationError.
	 * </p>
	 * @param globalErrors a {@link java.util.List} object.
	 */
	public void addValidationError(List<ObjectError> globalErrors) {
		globalErrors.forEach(this::addValidationError);
	}

	/**
	 * Utility method for adding error of ConstraintViolation. Usually when a @Validated
	 * validation fails.
	 * @param cv the ConstraintViolation
	 */
	private void addValidationError(ConstraintViolation<?> cv) {
		this.addValidationError(cv.getRootBeanClass().getSimpleName(),
				((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv.getInvalidValue(), cv.getMessage());
	}

	/**
	 * <p>
	 * addValidationErrors.
	 * </p>
	 * @param constraintViolations a {@link java.util.Set} object.
	 */
	public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
		constraintViolations.forEach(this::addValidationError);
	}

	/**
	 * Marker Interface.
	 *
	 * @author Raja kolli
	 *
	 */
	private interface ApiSubError {

	}

	/**
	 * ApiValidationError.
	 *
	 * @author Raja Kolli
	 *
	 */
	@Data
	@EqualsAndHashCode
	@AllArgsConstructor
	static class ApiValidationError implements ApiSubError {

		private String object;

		private String field;

		private Object rejectedValue;

		private String message;

		ApiValidationError(String object, String message) {
			this.object = object;
			this.message = message;
		}

	}

}
