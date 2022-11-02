/* Licensed under Apache-2.0 2022 */
package com.mongodb.redis.integration.config;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public abstract class AbstractIntegrationTest extends AbstractRedisTestContainer {

    @Autowired protected WebTestClient webTestClient;
}
