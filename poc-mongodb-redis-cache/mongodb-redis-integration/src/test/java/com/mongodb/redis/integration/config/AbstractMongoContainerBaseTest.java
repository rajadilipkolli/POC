package com.mongodb.redis.integration.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractMongoContainerBaseTest {

    static DockerImageName mongoDockerImageName = DockerImageName.parse("mongo:5.0");

    @Container
    protected static final MongoDBContainer MONGO_DB_CONTAINER =
            new MongoDBContainer(mongoDockerImageName).withExposedPorts(27017);

    static {
        MONGO_DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setMongoDbContainerURI(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.data.mongodb.host", MONGO_DB_CONTAINER::getHost);
        propertyRegistry.add("spring.data.mongodb.port", MONGO_DB_CONTAINER::getFirstMappedPort);
    }
}
