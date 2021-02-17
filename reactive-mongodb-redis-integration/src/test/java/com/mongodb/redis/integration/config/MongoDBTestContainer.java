package com.mongodb.redis.integration.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public abstract class MongoDBTestContainer {

  static DockerImageName dockerImageName = DockerImageName.parse("mongo");

  @Container
  protected static final MongoDBContainer MONGO_DB_CONTAINER =
      new MongoDBContainer(dockerImageName).withExposedPorts(27017);

  static {
    MONGO_DB_CONTAINER.start();
  }

  @DynamicPropertySource
  static void setMongoDbContainerURI(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("spring.data.mongodb.host", MONGO_DB_CONTAINER::getHost);
    propertyRegistry.add("spring.data.mongodb.port", MONGO_DB_CONTAINER::getFirstMappedPort);
  }
}
