/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoDBRedisReactiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoDBRedisReactiveApplication.class, args);
    }
}
