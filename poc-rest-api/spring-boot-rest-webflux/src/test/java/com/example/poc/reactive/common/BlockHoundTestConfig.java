/* Licensed under Apache-2.0 2025 */
package com.example.poc.reactive.common;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import reactor.blockhound.BlockHound;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BlockHoundTestConfig {
    @BeforeAll
    void setUpBlockHound() {
        // PostgreSQL SCRAM nonce generation can read JDK entropy on the Netty event loop.
        BlockHound.install(
                builder ->
                        builder.allowBlockingCallsInside(
                                "com.ongres.scram.common.ScramFunctions", "nonce"));
    }
}
