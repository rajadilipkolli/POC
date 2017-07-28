package com.poc.restfulpoc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor // Only to be compliant with JPA
public class Address {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String street;

    private String town;

    private String county;

    private String postcode;

    public Address(String street, String town, String county, String postcode) {
        this.street = street;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

}
