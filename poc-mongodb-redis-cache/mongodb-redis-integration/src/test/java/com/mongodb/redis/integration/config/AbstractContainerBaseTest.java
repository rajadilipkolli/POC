/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Testcontainers(disabledWithoutDocker = true)
public abstract class AbstractContainerBaseTest {

    static DockerImageName redisDockerImageName = DockerImageName.parse("redis");

    @Container
    @ServiceConnection(name = "redis")
    protected static final GenericContainer REDIS_DB_CONTAINER =
            new GenericContainer(redisDockerImageName).withExposedPorts(6379);

    static DockerImageName mongoDockerImageName = DockerImageName.parse("mongo:6.0.2");

    @Container @ServiceConnection
    protected static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(mongoDockerImageName)
                    .withSharding()
                    .withStartupAttempts(3)
                    .withStartupTimeout(Duration.ofMinutes(2));
}
