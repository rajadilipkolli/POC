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

package com.poc.restfulpoc.jooq;

import java.util.concurrent.atomic.AtomicBoolean;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.poc.restfulpoc.data.DataBuilder;
import com.poc.restfulpoc.repository.CustomerRepository;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static com.poc.restfulpoc.jooq.tables.Customer.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(Lifecycle.PER_CLASS)
class JooqTransactionTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private DSLContext dsl;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private DataBuilder dataBuilder;

	private DataSourceTransactionManager txMgr;

	@BeforeAll
	public void init() {
		this.txMgr = new DataSourceTransactionManager(
				new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build());
	}

	@BeforeEach
	public void setUp() throws Exception {
		this.customerRepository.deleteAll();
		this.dataBuilder.run();
	}

	@Test
	public void testExplicitTransactions() {
		boolean rollback = false;

		TransactionStatus tx = this.txMgr
				.getTransaction(new DefaultTransactionDefinition());
		try {

			// This is a "bug". The same CUSTOMER is created twice, resulting in a
			// constraint violation exception
			for (int i = 0; i < 2; i++) {
				this.dsl.insertInto(CUSTOMER).set(CUSTOMER.ID, 5L)
						.set(CUSTOMER.LAST_NAME, "1")
						.set(CUSTOMER.FIRST_NAME, "CUSTOMER 5").execute();
			}
			Assertions.fail();
		}

		// Upon the constraint violation, we explicitly roll back the transaction.
		catch (DataAccessException ex) {
			this.txMgr.rollback(tx);
			rollback = true;
		}

		assertThat(this.dsl.fetchCount(CUSTOMER)).isEqualTo(4);
		assertThat(rollback).isTrue();
	}

	@Test
	public void testjOOQTransactionsSimple() {
		boolean rollback = false;

		try {
			this.dsl.transaction((c) -> {

				// This is a "bug". The same CUSTOMER is created twice, resulting in a
				// constraint violation exception
				for (int i = 0; i < 2; i++) {
					this.dsl.insertInto(CUSTOMER).set(CUSTOMER.ID, 5L)
							.set(CUSTOMER.LAST_NAME, "1")
							.set(CUSTOMER.FIRST_NAME, "CUSTOMER 5").execute();
				}
				Assertions.fail();
			});
		}

		// Upon the constraint violation, the transaction must already have been rolled
		// back
		catch (DataAccessException ex) {
			rollback = true;
		}

		assertThat(this.dsl.fetchCount(CUSTOMER)).isEqualTo(3);
		assertThat(rollback).isTrue();
	}

	@Test
	@Disabled
	public void testjOOQTransactionsNested() {
		AtomicBoolean rollback1 = new AtomicBoolean(false);
		AtomicBoolean rollback2 = new AtomicBoolean(false);

		try {

			// If using Spring transactions, we don't need the c1 reference
			this.dsl.transaction((c1) -> {

				// The first insertion will work
				this.dsl.insertInto(CUSTOMER).set(CUSTOMER.ID, 5L)
						.set(CUSTOMER.LAST_NAME, "1")
						.set(CUSTOMER.FIRST_NAME, "CUSTOMER 5").execute();

				assertThat(this.dsl.fetchCount(CUSTOMER)).isEqualTo(4);

				try {

					// Nest transactions using Spring. This should create a savepoint,
					// right here
					// If using Spring transactions, we don't need the c2 reference
					this.dsl.transaction((c2) -> {

						// The second insertion shouldn't work
						for (int i = 0; i < 2; i++) {
							this.dsl.insertInto(CUSTOMER).set(CUSTOMER.ID, 6L)
									.set(CUSTOMER.LAST_NAME, "1")
									.set(CUSTOMER.FIRST_NAME, "CUSTOMER 6").execute();
						}
						Assertions.fail();
					});
				}

				catch (DataAccessException ex) {
					rollback1.set(true);
				}

				// We should've rolled back to the savepoint
				assertThat(this.dsl.fetchCount(CUSTOMER)).isEqualTo(4);

				throw new org.jooq.exception.DataAccessException("Rollback");
			});
		}

		// Upon the constraint violation, the transaction must already have been rolled
		// back
		catch (org.jooq.exception.DataAccessException ex) {
			assertThat(ex.getMessage()).isEqualTo("Rollback");
			rollback2.set(true);
		}

		assertThat(this.dsl.fetchCount(CUSTOMER)).isEqualTo(4);
		assertThat(rollback2.get()).isTrue();
		assertThat(rollback2.get()).isTrue();
	}

}
