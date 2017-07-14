package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MongoDbRedisIntegrationApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MongoDbRedisIntegrationApplication.class, args);
    }
}
