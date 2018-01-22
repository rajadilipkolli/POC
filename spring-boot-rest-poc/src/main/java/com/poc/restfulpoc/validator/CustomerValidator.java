/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.validator;

import java.util.Date;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.poc.restfulpoc.entities.Customer;

@Component
public class CustomerValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerValidator.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName",
                "message.firstName", "FirstName is Mandatory");
        final Customer customer = (Customer) target;
        final Date dob = customer.getDateOfBirth();
        if (Objects.nonNull(dob) && dob.after(new Date())) {
            errors.rejectValue("dateOfBirth", "NULL",
                    "Date Of Birth Should be before today");
            errors.reject("NULL", "Entity Not Processable");
        }
    }

}
