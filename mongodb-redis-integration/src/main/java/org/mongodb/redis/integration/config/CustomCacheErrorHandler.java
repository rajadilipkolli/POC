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

package org.mongodb.redis.integration.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * CustomCacheErrorHandler class.
 *
 * @author Raja Kolli
 * @version 0 : 5
 * @since July 2017
 */

@Slf4j
class CustomCacheErrorHandler implements CacheErrorHandler {

	/** {@inheritDoc} */
	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		log.error("Cache {} is down to search for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	/** {@inheritDoc} */
	@Override
	public void handleCachePutError(RuntimeException exception, Cache cache, Object key,
			Object value) {
		log.error("Cache {} is down to put for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	/** {@inheritDoc} */
	@Override
	public void handleCacheEvictError(RuntimeException exception, Cache cache,
			Object key) {
		log.error("Cache {} is down to evict for key :{} with exception :{}",
				cache.getName(), key, exception.getMessage());
	}

	/** {@inheritDoc} */
	@Override
	public void handleCacheClearError(RuntimeException exception, Cache cache) {
		log.error("Cache {} is down to clear with exception :{}", cache.getName(),
				exception.getMessage());
	}

}
