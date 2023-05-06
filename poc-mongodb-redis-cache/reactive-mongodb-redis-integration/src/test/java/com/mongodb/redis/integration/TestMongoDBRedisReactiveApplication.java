/* Licensed under Apache-2.0 2023 */
package com.mongodb.redis.integration;

import com.mongodb.redis.integration.config.MyContainersConfiguration;

import org.springframework.boot.SpringApplication;

public class TestMongoDBRedisReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.from(MongoDBRedisReactiveApplication::main)
                .with(MyContainersConfiguration.class)
                .run(args);
    }
}
