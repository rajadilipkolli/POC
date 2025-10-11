/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.common;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractPostgreSQLContainerBase {

    @Container
    protected static final PostgreSQLContainer<?> sqlContainer =
            new PostgreSQLContainer<>("postgres:18-alpine");

    static {
        sqlContainer.start();
    }

    @DynamicPropertySource
    static void setSqlContainer(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", sqlContainer::getUsername);
        propertyRegistry.add("spring.datasource.password", sqlContainer::getPassword);
    }
}
