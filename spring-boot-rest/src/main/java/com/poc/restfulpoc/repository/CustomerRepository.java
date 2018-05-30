/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.repository;

import java.util.List;

import com.poc.restfulpoc.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 * CustomerRepository interface.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * <p>
	 * findByFirstName.
	 * </p>
	 * @param firstName a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	List<Customer> findByFirstName(String firstName);

}
