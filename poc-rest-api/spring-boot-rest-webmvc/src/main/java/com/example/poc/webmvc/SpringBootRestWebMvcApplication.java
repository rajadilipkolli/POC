/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc;

import com.example.poc.webmvc.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.poc.webmvc.entities"})
@EnableConfigurationProperties({ApplicationProperties.class})
public class SpringBootRestWebMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestWebMvcApplication.class, args);
    }
}
