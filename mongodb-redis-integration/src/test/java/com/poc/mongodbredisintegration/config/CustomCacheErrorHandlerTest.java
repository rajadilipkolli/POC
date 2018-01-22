/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.poc.mongodbredisintegration.AbstractMongoDBRedisIntegrationTest;

public class CustomCacheErrorHandlerTest extends AbstractMongoDBRedisIntegrationTest  {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private Cache cache;

    private CacheInterceptor cacheInterceptor;

    private SimpleService simpleService;

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        this.cache = context.getBean("mockCache", Cache.class);
        this.cacheInterceptor = context.getBean(CacheInterceptor.class);
        this.simpleService = context.getBean(SimpleService.class);
    }

    @AfterEach
    public void tearDown() {
        context.close();
    }

    @Test
    public void getFail() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on get");
        willThrow(exception).given(this.cache).get(0L);

        final Object result = this.simpleService.get(0L);
        verify(this.cache).get(0L);
        verify(this.cache).put(0L, result); // result of the invocation
    }

    @Test
    public void getAndPutFail() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on get");
        willThrow(exception).given(this.cache).get(0L);
        willThrow(exception).given(this.cache).put(0L, 0L); // Update of the cache will
                                                            // fail as well

        final Object counter = this.simpleService.get(0L);

        willReturn(new SimpleValueWrapper(2L)).given(this.cache).get(0L);
        final Object counter2 = this.simpleService.get(0L);
        final Object counter3 = this.simpleService.get(0L);
        assertThat(counter2).isEqualTo(counter3);
        assertThat(counter).isNotEqualTo(counter2);
    }

    @Test
    public void getFailProperException() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on get");
        willThrow(exception).given(this.cache).get(0L);

        this.cacheInterceptor.setErrorHandler(new SimpleCacheErrorHandler());
        this.thrown.expect(is(exception));
//        this.simpleService.get(0L);
    }

    @Test
    public void putFail() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on put");
        willThrow(exception).given(this.cache).put(0L, 0L);

        this.simpleService.put(0L);
    }

    @Test
    public void putFailProperException() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on put");
        willThrow(exception).given(this.cache).put(0L, 0L);

        this.cacheInterceptor.setErrorHandler(new SimpleCacheErrorHandler());

        this.thrown.expect(is(exception));
//        this.simpleService.put(0L);
    }

    @Test
    public void evictFail() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on evict");
        willThrow(exception).given(this.cache).evict(0L);

        this.simpleService.evict(0L);
    }

    @Test
    public void evictFailProperException() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on evict");
        willThrow(exception).given(this.cache).evict(0L);

        this.cacheInterceptor.setErrorHandler(new SimpleCacheErrorHandler());

        this.thrown.expect(is(exception));
//        this.simpleService.evict(0L);
    }

    @Test
    public void clearFail() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on evict");
        willThrow(exception).given(this.cache).clear();

        this.simpleService.clear();
    }

    @Test
    public void clearFailProperException() {
        final UnsupportedOperationException exception = new UnsupportedOperationException(
                "Test exception on evict");
        willThrow(exception).given(this.cache).clear();

        this.cacheInterceptor.setErrorHandler(new SimpleCacheErrorHandler());

        this.thrown.expect(is(exception));
//        this.simpleService.clear();
    }

    @TestConfiguration
    @EnableCaching
    static class Config extends CachingConfigurerSupport {

        @Bean
        @Override
        public CacheErrorHandler errorHandler() {
            return new CustomCacheErrorHandler();
        }

        @Bean
        public SimpleService simpleService() {
            return new SimpleService();
        }

        @Bean
        public CacheManager cacheManager() {
            final SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(Collections.singletonList(mockCache()));
            return cacheManager;
        }

        @Bean
        public Cache mockCache() {
            final Cache cache = mock(Cache.class);
            given(cache.getName()).willReturn("test");
            return cache;
        }

    }

    @CacheConfig(cacheNames = "test")
    public static class SimpleService {
        private AtomicLong counter = new AtomicLong();

        @Cacheable
        public Object get(long id) {
            return this.counter.getAndIncrement();
        }

        @CachePut
        public Object put(long id) {
            return this.counter.getAndIncrement();
        }

        @CacheEvict
        public void evict(long id) {
            this.counter.decrementAndGet();
        }

        @CacheEvict(allEntries = true)
        public void clear() {
            this.counter = new AtomicLong(0);
        }
    }

}
