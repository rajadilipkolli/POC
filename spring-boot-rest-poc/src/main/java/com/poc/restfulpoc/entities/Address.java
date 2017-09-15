/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Address class.</p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@Setter
@Getter
@Entity
@NoArgsConstructor // Only to be compliant with JPA
public class Address {

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
    private long id;

    private String street;

    private String town;

    private String county;

    private String postcode;

    /**
     * <p>Constructor for Address.</p>
     *
     * @param street a {@link java.lang.String} object.
     * @param town a {@link java.lang.String} object.
     * @param county a {@link java.lang.String} object.
     * @param postcode a {@link java.lang.String} object.
     */
    public Address(String street, String town, String county, String postcode) {
        this.street = street;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

}
