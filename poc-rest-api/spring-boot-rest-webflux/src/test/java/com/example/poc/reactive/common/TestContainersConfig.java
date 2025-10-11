/* Licensed under Apache-2.0 2023 */
package com.example.poc.reactive.common;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

public interface TestContainersConfig {

    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:18-alpine");
}
