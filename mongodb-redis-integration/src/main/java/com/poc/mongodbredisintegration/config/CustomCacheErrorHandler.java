/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.config;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 *
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

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
