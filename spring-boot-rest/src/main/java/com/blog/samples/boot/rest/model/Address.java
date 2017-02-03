package com.blog.samples.boot.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Address
{

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String street;

    private String town;

    private String county;

    private String postcode;

    public Address()
    {
        // JPA needs no Args Constructor
    }

    public Address(String street, String town, String county, String postcode)
    {
        this.street = street;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }
}