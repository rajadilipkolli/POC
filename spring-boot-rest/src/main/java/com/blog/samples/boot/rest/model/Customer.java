package com.blog.samples.boot.rest.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Customer{

	public Customer(){}
	
	public Customer(String firstName, String lastName, Date dateOfBirth, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}


	@Id
	@Getter
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	@Setter
	@Getter
	private String firstName;
	
	@Setter
	@Getter
	private String lastName;
	
	@Setter	
	@Getter
	private Date dateOfBirth;

	@Setter
	@Getter
	@OneToOne(cascade = {CascadeType.ALL})
	private Address address;
}