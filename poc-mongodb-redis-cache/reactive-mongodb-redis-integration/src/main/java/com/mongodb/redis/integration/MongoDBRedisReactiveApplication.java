/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class MongoDBRedisReactiveApplication {
    public static void main(String[] args) {
        BlockHound.builder()
                .allowBlockingCallsInside(
                        "org.springframework.validation.beanvalidation.SpringValidatorAdapter",
                        "validate")
                .allowBlockingCallsInside("jakarta.validation.Validator", "validate")
                .allowBlockingCallsInside("org.slf4j.LoggerFactory", "getLogger")
                .allowBlockingCallsInside("java.util.UUID", "randomUUID")
                .install();

        SpringApplication.run(MongoDBRedisReactiveApplication.class, args);
    }
}
