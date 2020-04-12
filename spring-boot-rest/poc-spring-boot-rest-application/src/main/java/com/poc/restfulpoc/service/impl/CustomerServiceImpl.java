/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poc.restfulpoc.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.poc.restfulpoc.entities.Customer;
import com.poc.restfulpoc.entities.Order;
import com.poc.restfulpoc.exception.EntityNotFoundException;
import com.poc.restfulpoc.repository.CustomerRepository;
import com.poc.restfulpoc.repository.OrderRepository;
import com.poc.restfulpoc.service.CustomerService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * CustomerServiceImpl class.
 * </p>
 *
 * @author Raja Kolli
 * @version 0: 5
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	private final OrderRepository orderRepository;

	private final JmsTemplate jmsTemplate;

	/** {@inheritDoc} */
	@Override
	@Cacheable(value = "customer", key = "#customerId", unless = "#result == null")
	public Customer getCustomer(Long customerId) throws EntityNotFoundException {
		return this.customerRepository.findById(customerId)
				.orElseThrow(() -> new EntityNotFoundException(Customer.class, "id", customerId.toString()));
	}

	/** {@inheritDoc} */
	@Override
	public List<Customer> getCustomers() {
		return this.customerRepository.findAll();
	}

	/** {@inheritDoc} */
	@Override
	@Transactional
	public Customer createCustomer(Customer customer) {
		return this.customerRepository.save(customer);
	}

	/** {@inheritDoc} */
	@Override
	@CachePut(value = "customer", key = "#customerId", unless = "#result == null")
	public Customer updateCustomer(Customer updateCustomerRequest, Long customerId) throws EntityNotFoundException {

		Customer persistedCustomer = getCustomer(customerId);
		persistedCustomer.setFirstName(updateCustomerRequest.getFirstName());
		persistedCustomer.setLastName(updateCustomerRequest.getLastName());
		persistedCustomer.setDateOfBirth(updateCustomerRequest.getDateOfBirth());
		persistedCustomer.setAddress(updateCustomerRequest.getAddress());

		// Remove the existing database rows that are no
		// longer found in the incoming collection (updateCustomerRequest.getOrders())
		List<Order> ordersToRemove = persistedCustomer.getOrders().stream()
				.filter(order -> !updateCustomerRequest.getOrders().contains(order)).collect(Collectors.toList());
		ordersToRemove.forEach(persistedCustomer::removeOrder);

		// Update the existing database rows which can be found
		// in the incoming collection (updateCustomerRequest.getOrders())
		List<Order> newOrders = updateCustomerRequest.getOrders().stream()
				.filter(order -> !persistedCustomer.getOrders().contains(order)).collect(Collectors.toList());

		updateCustomerRequest.getOrders().stream().filter(order -> !newOrders.contains(order)).forEach((order) -> {
			order.setCustomer(persistedCustomer);
			Order mergedOrder = this.orderRepository.save(order);
			persistedCustomer.getOrders().set(persistedCustomer.getOrders().indexOf(mergedOrder), mergedOrder);
		});

		// Add the rows found in the incoming collection,
		// which cannot be found in the current database snapshot
		newOrders.forEach(persistedCustomer::addOrder);

		return this.customerRepository.save(persistedCustomer);
	}

	/** {@inheritDoc} */
	@Override
	@CacheEvict(value = "customer", key = "#customerId")
	public void deleteCustomerById(Long customerId) throws EntityNotFoundException {
		getCustomer(customerId);
		// Using JMS Template as the call can be asynchronous
		this.jmsTemplate.convertAndSend("jms.message.endpoint", customerId);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCustomerExist(String firstName) {
		final List<Customer> customerList = this.customerRepository.findByFirstName(firstName);
		return !customerList.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	@CacheEvict(value = "customer", allEntries = true)
	public void deleteAllCustomers() {
		this.customerRepository.deleteAll();
	}

}
