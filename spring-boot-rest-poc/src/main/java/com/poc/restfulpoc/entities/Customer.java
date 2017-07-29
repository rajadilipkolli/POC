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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor // Only to be compliant with JPA
public class Customer {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    @OneToOne(cascade = { CascadeType.ALL })
    private Address address;

    public Customer(String firstName, String lastName, Date dateOfBirth,
            Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = (Date) dateOfBirth.clone();
        this.address = address;
    }

    public Date getDateOfBirth() {
        if (dateOfBirth == null) {
            return null;
        } else {
            return new Date(dateOfBirth.getTime());
        }
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = (Date) dateOfBirth.clone();
    }
}
