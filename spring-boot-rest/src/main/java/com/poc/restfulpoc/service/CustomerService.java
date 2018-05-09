/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.service;

import java.util.List;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.exception.EntityNotFoundException;
/**
 * <p>CustomerService interface.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
public interface CustomerService {

    /**
     * <p>getCustomer.</p>
     *
     * @param customerId a {@link java.lang.Long} object.
     * @return a {@link com.poc.restfulpoc.entities.Customer} object.
     * @throws com.poc.restfulpoc.exception.EntityNotFoundException if any.
     */
    Customer getCustomer(Long customerId) throws EntityNotFoundException;

    /**
     * <p>getCustomers.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<Customer> getCustomers();

    /**
     * <p>createCustomer.</p>
     *
     * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
     * @return a {@link com.poc.restfulpoc.entities.Customer} object.
     */
    Customer createCustomer(Customer customer);

    /**
     * <p>updateCustomer.</p>
     *
     * @param customer a {@link com.poc.restfulpoc.entities.Customer} object.
     * @return a {@link com.poc.restfulpoc.entities.Customer} object.
     * @throws EntityNotFoundException 
     */
    Customer updateCustomer(Customer customer, Long customerId) throws EntityNotFoundException;

    /**
     * <p>deleteCustomerById.</p>
     *
     * @param customerId a {@link java.lang.Long} object.
     * @throws EntityNotFoundException 
     */
    void deleteCustomerById(Long customerId) throws EntityNotFoundException;

    /**
     * <p>isCustomerExist.</p>
     *
     * @param firstName a {@link com.poc.restfulpoc.entities.Customer} object.
     * @return a boolean.
     */
    boolean isCustomerExist(String firstName);

    /**
     * <p>deleteAllCustomers.</p>
     */
    void deleteAllCustomers();

}
