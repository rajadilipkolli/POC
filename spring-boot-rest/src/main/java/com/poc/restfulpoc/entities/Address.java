/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Required for Builder
public class Address {

    @Id
    private long id;

    private String street;

    private String town;

    private String county;

    private String postcode;
    
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
