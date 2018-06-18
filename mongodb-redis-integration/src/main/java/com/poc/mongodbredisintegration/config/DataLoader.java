/*
 * Copyright 2015-2018 the original author or authors.
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

package com.poc.mongodbredisintegration.config;

import java.util.ArrayList;
import java.util.List;

import com.poc.mongodbredisintegration.controller.MongoDBRedisIntegrationController;
import com.poc.mongodbredisintegration.document.Book;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * DataLoader class.
 * </p>
 *
 * @author Raja Kolli
 * @since 0.0.5
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

	private final MongoDBRedisIntegrationController controller;

	/** {@inheritDoc} */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		final Long cnt = this.controller.count();
		if (cnt == 0) {
			this.controller.deleteCache();
			Book book = Book.builder().title("MongoDbCookBook").text("MongoDB Data Book")
					.author("Raja").bookId("1").build();
			this.controller.saveBook(book);

			final List<Book> bookList = new ArrayList<>();
			for (int i = 0; i < 100; i++) {
				book = new Book();
				book.setTitle(RandomStringUtils.randomAlphanumeric(20));
				book.setText(RandomStringUtils.randomAlphanumeric(30));
				bookList.add(book);
			}
			this.controller.saveAllBooks(bookList);
		}
	}

}
