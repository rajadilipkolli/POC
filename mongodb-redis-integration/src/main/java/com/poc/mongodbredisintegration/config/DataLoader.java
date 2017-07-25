package com.poc.mongodbredisintegration.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final MongoDBRedisIntegrationController controller;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Long cnt = controller.count();
        if (cnt == 0) {
            controller.deleteCache();
            Book book = new Book();
            book.setTitle("MongoDbCookBook");
            book.setText("MongoDB Data Book");
            book.setAuthor("Raja");
            controller.saveBook(book);
        }
    }
}
