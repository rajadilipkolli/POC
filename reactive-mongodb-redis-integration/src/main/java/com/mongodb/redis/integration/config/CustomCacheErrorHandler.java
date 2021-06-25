package com.mongodb.redis.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j
class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.error(
                "Cache {} is down to search for key :{} with exception ",
                cache.getName(),
                key,
                exception);
    }

    @Override
    public void handleCachePutError(
            RuntimeException exception, Cache cache, Object key, Object value) {
        log.error(
                "Cache {} is down to put for key :{} with exception ",
                cache.getName(),
                key,
                exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.error(
                "Cache {} is down to evict for key :{} with exception ",
                cache.getName(),
                key,
                exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Cache {} is down to clear", cache.getName(), exception);
    }
}
