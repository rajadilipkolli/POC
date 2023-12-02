/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration.config;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public interface MongoDBTestContainerConfig {

    @ServiceConnection
    MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo").withTag("7.0.4"))
                    .withSharding()
                    .withStartupAttempts(3)
                    .withStartupTimeout(Duration.ofMinutes(2))
                    .withReuse(true);
}
