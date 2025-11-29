/* Licensed under Apache-2.0 2022 */
package com.mongodb.redis.integration.config;

import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import com.mongodb.redis.integration.repository.ReactiveItemCappedRepository;
import com.mongodb.redis.integration.repository.ReactiveItemRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestContainersConfig.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public abstract class AbstractIntegrationTest extends BlockHoundTestConfig {

    @Autowired protected WebTestClient webTestClient;

    @Autowired protected ReactiveMongoOperations operations;

    @Autowired protected ReactiveBookRepository reactiveBookRepository;

    @Autowired protected ReactiveItemCappedRepository reactiveItemCappedRepository;

    @Autowired protected ReactiveItemRepository reactiveItemRepository;

    @Autowired protected MongoOperations mongoOperations;
}
