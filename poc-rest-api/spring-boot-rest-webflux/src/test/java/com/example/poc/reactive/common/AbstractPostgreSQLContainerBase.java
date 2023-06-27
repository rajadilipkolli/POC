/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.common;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class AbstractPostgreSQLContainerBase {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15.3-alpine");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void addApplicationProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add(
                "spring.r2dbc.url",
                () ->
                        "r2dbc:postgresql://"
                                + postgreSQLContainer.getHost()
                                + ":"
                                + postgreSQLContainer.getFirstMappedPort()
                                + "/"
                                + postgreSQLContainer.getDatabaseName());
        propertyRegistry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
        propertyRegistry.add(
                "spring.liquibase.url",
                () ->
                        "jdbc:postgresql://"
                                + postgreSQLContainer.getHost()
                                + ":"
                                + postgreSQLContainer.getFirstMappedPort()
                                + "/"
                                + postgreSQLContainer.getDatabaseName());
        propertyRegistry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        propertyRegistry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    }
}
