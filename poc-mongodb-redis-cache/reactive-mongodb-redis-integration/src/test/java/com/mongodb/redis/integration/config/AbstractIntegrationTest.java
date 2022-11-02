package com.mongodb.redis.integration.config;

import com.mongodb.redis.integration.config.AbstractRedisTestContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public abstract class AbstractIntegrationTest extends AbstractRedisTestContainer {

    @Autowired protected WebTestClient webTestClient;
    
}
