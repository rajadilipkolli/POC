/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.poc.restfulpoc.data.DataBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { RestFulPOCApplication.class,
      DataBuilder.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractRestFulPOCApplicationTest {

    
    @Autowired
    private Environment environment;
    
    protected TestRestTemplate restTemplate() {
        return configure(new TestRestTemplate());
    }

    protected TestRestTemplate adminRestTemplate() {
        return configure(new TestRestTemplate("admin", "admin"));
    }

    protected TestRestTemplate userRestTemplate() {
        return configure(new TestRestTemplate("username", "password"));
    }

    private TestRestTemplate configure(TestRestTemplate restTemplate) {
        restTemplate
                .setUriTemplateHandler(new LocalHostUriTemplateHandler(this.environment));
        return restTemplate;
    }

}
