package com.example.poc.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.logging.logback.ColorConverter;
import org.springframework.nativex.hint.InitializationHint;
import org.springframework.nativex.hint.InitializationTime;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@InitializationHint(types = ColorConverter.class, initTime = InitializationTime.BUILD)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
