package com.mongodb.redis.integration.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractRedisTestContainer extends MongoDBTestContainer {

    static DockerImageName redisDockerImageName = DockerImageName.parse("redis");

    @Container
    protected static final GenericContainer REDIS_DB_CONTAINER =
            new GenericContainer(redisDockerImageName).withExposedPorts(6379);

    static {
        REDIS_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setMongoDbContainerURI(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.redis.host", REDIS_DB_CONTAINER::getHost);
        propertyRegistry.add("spring.redis.port", REDIS_DB_CONTAINER::getFirstMappedPort);
    }
}
