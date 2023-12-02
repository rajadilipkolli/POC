/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration;

import com.mongodb.redis.integration.config.MongoDBTestContainerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@Import(MongoDBTestContainerConfig.class)
public class TestMongoDBRedisReactiveApplication {

    @Bean
    @ServiceConnection(name = "redis")
    public GenericContainer redisContainer() {
        return new GenericContainer(DockerImageName.parse("redis").withTag("7.2.3-alpine"))
                .withExposedPorts(6379);
    }

    public static void main(String[] args) {
        SpringApplication.from(MongoDBRedisReactiveApplication::main)
                .with(TestMongoDBRedisReactiveApplication.class)
                .run(args);
    }
}
