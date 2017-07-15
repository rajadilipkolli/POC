/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.controller.WebServicesController;
import com.example.model.Book;

import lombok.RequiredArgsConstructor;

/**
 * This shouldn't be used in production system, used only for demonstration purposes
 * 
 * @author rajakolli
 *
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

	private final WebServicesController controller;

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
