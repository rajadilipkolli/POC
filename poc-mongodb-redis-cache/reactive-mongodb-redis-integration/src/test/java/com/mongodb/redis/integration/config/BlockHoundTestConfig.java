/* Licensed under Apache-2.0 2025 */
package com.mongodb.redis.integration.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import reactor.blockhound.BlockHound;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BlockHoundTestConfig {
    @BeforeAll
    void setUpBlockHound() {
        BlockHound.install();
    }
}
