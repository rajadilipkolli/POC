/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import javax.sql.DataSource;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for DataSourceProxyBeanConfig.
 *
 * @author Raja Kolli
 *
 */
public class DataSourceProxyBeanConfigTest extends AbstractRestFulPOCApplicationTest {

	@Autowired
	private DataSource datasource;

	@Test
	public void test() {
		assertThat(this.datasource).isNotNull();
		assertThat(this.datasource).isInstanceOf(HikariDataSource.class);
	}

}
