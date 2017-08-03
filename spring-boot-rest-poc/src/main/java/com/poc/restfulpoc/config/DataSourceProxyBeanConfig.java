/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.listener.logging.SystemOutQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

@Slf4j
@Configuration
public class DataSourceProxyBeanConfig {

    @Bean
    public DataSource customDataSource(DataSourceProperties properties) {
        log.info("Inside Proxy Creation");

        final HikariDataSource dataSource = (HikariDataSource) properties
                .initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (properties.getName() != null) {
            dataSource.setPoolName(properties.getName());
        }
        // use pretty formatted query with multiline enabled
        final PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
        creator.setMultiline(true);
        final SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
        listener.setQueryLogEntryCreator(creator);

        return ProxyDataSourceBuilder.create(dataSource).countQuery().name("MyDS")
                .listener(listener).logSlowQueryToSysOut(1, TimeUnit.MINUTES).build();
    }
}
