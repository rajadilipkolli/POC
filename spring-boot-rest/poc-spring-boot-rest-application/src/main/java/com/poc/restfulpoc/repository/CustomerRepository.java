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

package com.poc.restfulpoc.repository;

import java.util.List;
import java.util.Optional;

import com.poc.restfulpoc.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
	 * finds {@link Customer} By FirstName.
	 * @param firstName a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	List<Customer> findByFirstName(String firstName);

	@Transactional(readOnly = true)
	@Query("select id from Customer c where c.firstName = :firstName")
	Optional<Long> findOptionalIdByFirstName(@Param("firstName") String firstName);

}
