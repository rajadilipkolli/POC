/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p> Customer class. </p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@Setter
@Getter
@Entity
@NoArgsConstructor // Only to be compliant with JPA
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required for Builder
public class Customer {

    // @formatter:off
    @Id
    @Getter
    @GenericGenerator(
            name = "sequenceGenerator", 
            strategy = "enhanced-sequence",
            parameters = {
                @org.hibernate.annotations.Parameter(
                    name = "optimizer",
                    value = "pooled-lo"
                ),
                @org.hibernate.annotations.Parameter(
                    name = "initial_value", 
                    value = "1"
                ),
                @org.hibernate.annotations.Parameter(
                    name = "increment_size", 
                    value = "5"
                )
            }
        )
        @GeneratedValue(
            strategy = GenerationType.SEQUENCE, 
            generator = "sequenceGenerator"
        )
    // @formatter:on
    private long id;

    @NotNull
    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    @OneToOne(cascade = { CascadeType.ALL })
    private Address address;

    /**
     * <p>
     * Getter for the field <code>dateOfBirth</code>.
     * </p>
     *
     * @return a {@link java.util.Date} object.
     */
    public Date getDateOfBirth() {
        if (dateOfBirth == null) {
            return null;
        } else {
            return new Date(dateOfBirth.getTime());
        }
    }

    /**
     * <p>
     * Setter for the field <code>dateOfBirth</code>.
     * </p>
     *
     * @param dateOfBirth a {@link java.util.Date} object.
     */
    public void setDateOfBirth(Date dateOfBirth) {
        if (null == dateOfBirth) {
            this.dateOfBirth = null;
        } else {
            this.dateOfBirth = (Date) dateOfBirth.clone();
        }
    }
}
