/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.restfulpoc.service;

import java.util.List;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;

/**
 * <p>
 * CustomerService interface.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
public interface CustomerService {

	/**
	 * <p>
	 * getCustomer.
	 * </p>
	 * @param customerId a {@link java.lang.Long} object.
	 * @return a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @throws EntityNotFoundException if any.
	 */
	Customer getCustomer(Long customerId) throws EntityNotFoundException;

	/**
	 * <p>
	 * getCustomers.
	 * </p>
	 * @return a {@link java.util.List} object.
	 */
	List<Customer> getCustomers();

	/**
	 * <p>
	 * createCustomer.
	 * </p>
	 * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @return a {@link com.poc.restfulpoc.entities.Customer} object.
	 */
	Customer createCustomer(Customer customer);

	/**
	 * <p>
	 * updateCustomer.
	 * </p>
	 * @param customerId a {@link java.lang.Long} object.
	 * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @return a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @throws EntityNotFoundException if any.
	 */
	Customer updateCustomer(Customer customer, Long customerId)
			throws EntityNotFoundException;

	/**
	 * <p>
	 * deleteCustomerById.
	 * </p>
	 * @param customerId a {@link java.lang.Long} object.
	 * @throws EntityNotFoundException if any.
	 */
	void deleteCustomerById(Long customerId) throws EntityNotFoundException;

	/**
	 * <p>
	 * isCustomerExist.
	 * </p>
	 * @param firstName a {@link com.poc.restfulpoc.entities.Customer} object.
	 * @return a boolean.
	 */
	boolean isCustomerExist(String firstName);

	/**
	 * <p>
	 * deleteAllCustomers.
	 * </p>
	 */
	void deleteAllCustomers();

}
