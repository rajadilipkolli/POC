/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration;

import com.mongodb.redis.integration.config.MongoDBTestContainerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(MongoDBTestContainerConfig.class)
public class TestMongoDBRedisApplication {

    @Bean
    @ServiceConnection(name = "redis")
    @RestartScope
    public GenericContainer redisContainer() {
        return new GenericContainer(DockerImageName.parse("redis").withTag("8.4.0-alpine"))
                .withExposedPorts(6379);
    }

    public static void main(String[] args) {
        SpringApplication.from(MongoDBRedisApplication::main)
                .with(TestMongoDBRedisApplication.class)
                .run(args);
    }
}
