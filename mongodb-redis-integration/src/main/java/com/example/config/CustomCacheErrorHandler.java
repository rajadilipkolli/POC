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

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Logic to handle CacheManager when it is down
 * 
 * @author rajakolli
 *
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		log.error("Cache {} is down to search for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
			Object value) {
		log.error("Cache {} is down to put for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache,
			Object key) {
		log.error("Cache {} is down to evict for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		log.error("Cache {} is down to clear with exception :{}", cache.getName(),
				exception.getMessage());
	}

}
