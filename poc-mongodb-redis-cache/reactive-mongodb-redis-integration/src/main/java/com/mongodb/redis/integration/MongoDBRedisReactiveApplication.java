/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class MongoDBRedisReactiveApplication {
    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(MongoDBRedisReactiveApplication.class, args);
    }
}
