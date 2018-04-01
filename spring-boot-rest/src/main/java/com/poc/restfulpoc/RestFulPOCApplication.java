/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>RestFulPOCApplication class.</p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@SpringBootApplication
public class RestFulPOCApplication {
    
    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        SpringApplication.run(RestFulPOCApplication.class, args);
    }
    
    @Bean
    public WebMvcConfigurer initializrWebMvcConfigurer() {
        return webMvcConfigurer();
    }

    private static final WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/info", "/actuator/info");
            }
        };
    }
    
}
