package com.example.poc.reactive.controller;

import com.example.poc.reactive.AbstractBasePostsEndpoints;
import com.example.poc.reactive.service.PostServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@Import({PostClassicController.class, PostServiceImpl.class})
class PostsControllerEndpoints extends AbstractBasePostsEndpoints {

    @BeforeAll
    static void before() {
        log.info("running classic tests");
    }

    PostsControllerEndpoints(@Autowired WebTestClient client) {
        super(client);
    }
}
