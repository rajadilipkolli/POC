/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.webmvc.common;

import static com.example.poc.webmvc.utils.AppConstants.PROFILE_IT;
import static com.example.poc.webmvc.utils.AppConstants.PROFILE_TEST;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({PROFILE_TEST, PROFILE_IT})
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest extends AbstractPostgreSQLContainerBase {

    @Autowired protected TestRestTemplate restTemplate;

    protected TestRestTemplate adminRestTemplate() {
        return restTemplate.withBasicAuth("admin", "admin");
    }

    protected TestRestTemplate userRestTemplate() {
        return restTemplate.withBasicAuth("username", "password");
    }

    @TestConfiguration
    static class Config {

        @Bean
        RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .readTimeout(Duration.ofSeconds(1));
        }
    }
}
