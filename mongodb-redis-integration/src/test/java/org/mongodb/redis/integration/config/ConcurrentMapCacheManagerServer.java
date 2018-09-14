package org.mongodb.redis.integration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ConcurrentMapCacheManagerServer {

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("books");
	}

}
