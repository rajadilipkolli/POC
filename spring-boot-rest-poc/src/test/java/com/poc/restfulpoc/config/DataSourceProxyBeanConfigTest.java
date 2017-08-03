/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import static org.assertj.core.api.Assertions.assertThat;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.poc.restfulpoc.AbstractRestFulPOCApplicationTest;

import net.ttddyy.dsproxy.support.ProxyDataSource;

public class DataSourceProxyBeanConfigTest extends AbstractRestFulPOCApplicationTest {

    @Autowired
    private DataSource datasource;

    @Test
    public void test() {
        assertThat(datasource).isNotNull();
        assertThat(datasource).isExactlyInstanceOf(ProxyDataSource.class);
    }

}
