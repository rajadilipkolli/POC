/* Licensed under Apache-2.0 2026 */
package com.mongodb.redis.integration.config;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class RedisTestContainerConfig {

    @Bean
    @ServiceConnection(name = "redis")
    @RestartScope
    public GenericContainer redisContainer() {
        return new GenericContainer(DockerImageName.parse("redis").withTag("8.6.3-alpine"))
                .withExposedPorts(6379);
    }
}
