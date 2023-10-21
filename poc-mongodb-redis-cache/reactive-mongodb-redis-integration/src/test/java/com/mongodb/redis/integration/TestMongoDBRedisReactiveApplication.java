/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration;

import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestMongoDBRedisReactiveApplication {

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo").withTag("7.0.2"))
                .withSharding()
                .withStartupAttempts(3)
                .withStartupTimeout(Duration.ofMinutes(2));
    }

    @Bean
    @ServiceConnection(name = "redis")
    public GenericContainer redisContainer() {
        return new GenericContainer(DockerImageName.parse("redis").withTag("7.2.2-alpine"))
                .withExposedPorts(6379);
    }

    public static void main(String[] args) {
        SpringApplication.from(MongoDBRedisReactiveApplication::main)
                .with(TestMongoDBRedisReactiveApplication.class)
                .run(args);
    }
}
