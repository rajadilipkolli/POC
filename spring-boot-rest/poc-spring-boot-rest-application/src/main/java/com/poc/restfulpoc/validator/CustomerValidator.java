/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.validator;

import java.time.LocalDateTime;
import java.util.Objects;

import com.poc.restfulpoc.entities.Customer;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * <p>
 * CustomerValidator class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
@Component
public class CustomerValidator implements Validator {

	private static final String ERROR_CODE = "VALIDATIONERROR";

	/** {@inheritDoc} */
	@Override
	public boolean supports(Class<?> clazz) {
		return CustomerValidator.class.isAssignableFrom(clazz);
	}

	/** {@inheritDoc} */
	@Override
	public void validate(@NonNull Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
				"message.firstName", "FirstName is Mandatory");
		final Customer customer = (Customer) target;
		final LocalDateTime dob = customer.getDateOfBirth();
		if (Objects.nonNull(dob) && dob.isAfter(LocalDateTime.now())) {
			errors.rejectValue("dateOfBirth", ERROR_CODE,
					"Date Of Birth Should be before today");
			errors.reject(ERROR_CODE, "Entity Not Processable");
		}
	}

}
