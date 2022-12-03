/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.common;

import static com.example.poc.webmvc.utils.AppConstants.PROFILE_IT;
import static com.example.poc.webmvc.utils.AppConstants.PROFILE_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({PROFILE_TEST, PROFILE_IT})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest extends AbstractPostgreSQLContainerBase {

    @Autowired private Environment environment;

    @Autowired private ObjectMapper mapper;

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
        restTemplate.setUriTemplateHandler(new LocalHostUriTemplateHandler(this.environment));
        return restTemplate;
    }

    @TestConfiguration
    static class Config {

        @Bean
        RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder()
                    .setConnectTimeout(Duration.ofSeconds(1))
                    .setReadTimeout(Duration.ofSeconds(1));
        }
    }
}
