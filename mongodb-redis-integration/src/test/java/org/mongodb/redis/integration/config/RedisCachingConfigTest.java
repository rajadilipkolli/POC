package org.mongodb.redis.integration.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mongodb.redis.integration.Application;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.mongodb.redis.integration.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { Application.class,
		ConcurrentMapCacheManagerServer.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureCache
public class RedisCachingConfigTest {

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
	public void caching() throws Exception {

		given(bookRepository.findBookByTitle(anyString()))
				.willReturn(Book.builder().title("prius").build());

		this.service.findBookByTitle("prius");
		this.service.findBookByTitle("prius");
		this.service.findBookByTitle("prius");

		verify(bookRepository, times(1)).findBookByTitle("prius");
	}

}
