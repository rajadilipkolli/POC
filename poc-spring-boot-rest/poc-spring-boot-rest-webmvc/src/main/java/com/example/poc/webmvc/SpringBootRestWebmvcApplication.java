package com.example.poc.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.poc.webmvc.entities"})
public class SpringBootRestWebmvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestWebmvcApplication.class, args);
    }
}
