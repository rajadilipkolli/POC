/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableTransactionManagement
public class Application {

    static {
        BlockHound.install();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
