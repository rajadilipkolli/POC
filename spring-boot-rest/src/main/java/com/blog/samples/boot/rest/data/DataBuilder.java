package com.blog.samples.boot.rest.data;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.blog.samples.boot.rest.model.Address;
import com.blog.samples.boot.rest.model.Customer;

@Component
public class DataBuilder {
	
	public List<Customer> createCustomers() {

		Customer customer1 = new Customer("Raja", "Kolli", DateTime.parse("1982-01-10").toDate(),
				new Address("High Street", "Belfast", "India", "BT893PY"));

		Customer customer2 = new Customer("Paul", "Jones", DateTime.parse("1973-01-03").toDate(),
				new Address("Main Street", "Lurgan", "Armagh", "BT283FG"));

		Customer customer3 = new Customer("Steve", "Toale", DateTime.parse("1979-03-08").toDate(),
				new Address("Main Street", "Newry", "Down", "BT359JK"));
		
		return Arrays.asList(customer1, customer2, customer3);
	}
}