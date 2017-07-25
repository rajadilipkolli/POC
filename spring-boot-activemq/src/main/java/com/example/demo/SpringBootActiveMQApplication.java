package com.example.demo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import com.example.demo.config.Message;

@SpringBootApplication
public class SpringBootActiveMQApplication {

    public static void main(String[] args) throws InterruptedException {
        // Launch the application
        ConfigurableApplicationContext context = SpringApplication
                .run(SpringBootActiveMQApplication.class, args);

        // Get JMS template bean reference
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        // Send a message
        System.out.println("Sending a message.");
        jmsTemplate.convertAndSend("jms.message.endpoint",
                new Message(1001L, "test body", new Date()));

        TimeUnit.SECONDS.sleep(1);
        SpringApplication.exit(context, new ExitCodeGenerator() {

            public int getExitCode() {
                return 0;
            }
        });
    }
}
