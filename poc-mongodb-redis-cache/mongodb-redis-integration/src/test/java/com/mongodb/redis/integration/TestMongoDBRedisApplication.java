/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration;

import com.mongodb.redis.integration.config.MongoDBTestContainerConfig;
import com.mongodb.redis.integration.config.RedisTestContainerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@ImportTestcontainers(MongoDBTestContainerConfig.class)
public class TestMongoDBRedisApplication {

    public static void main(String[] args) {
        SpringApplication.from(MongoDBRedisApplication::main)
                .with(RedisTestContainerConfig.class)
                .run(args);
    }
}
