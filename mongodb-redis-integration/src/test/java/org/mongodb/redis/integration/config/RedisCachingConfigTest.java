/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb.redis.integration.config;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mongodb.redis.integration.Application;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.mongodb.redis.integration.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = { Application.class, ConcurrentMapCacheManagerServer.class })
@AutoConfigureCache
class RedisCachingConfigTest {

	// Will require the spring application content for testing on the cache.
	// Therefore, will need to include the spring boot test.
	// But, since we do not require to test on the server, we will change the web
	// environment to none.

	// Can also specify the cache config class

	@Autowired
	private BookService service;

	@MockBean
	private BookRepository bookRepository;

	@Test
	void caching() throws Exception {

		given(this.bookRepository.findBookByTitle(anyString()))
				.willReturn(Optional.of(Book.builder().title("prius").build()));

		this.service.findBookByTitle("prius");
		this.service.findBookByTitle("prius");
		this.service.findBookByTitle("prius");

		verify(this.bookRepository, times(1)).findBookByTitle("prius");
	}

}
