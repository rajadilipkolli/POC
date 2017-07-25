/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 *
 */
@EnableCaching
@Configuration
public class RedisConfiguration extends CachingConfigurerSupport {

    /**
     * <p>jedisConnectionFactory.</p>
     *
     * @return a {@link org.springframework.data.redis.connection.jedis.JedisConnectionFactory} object.
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    /* redis template definition */
    /**
     * <p>redisTemplate.</p>
     *
     * @return a {@link org.springframework.data.redis.core.RedisTemplate} object.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    /*
     * declare Redis Cache Manager
     * 
     * Never load remote caches on startup as it can be corrupted
     * 
     * (non-Javadoc)
     * 
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#cacheManager()
     */
    /** {@inheritDoc} */
    @Bean
    @Override
    public RedisCacheManager cacheManager() {
        final RedisCacheManager redisCacheManager = new RedisCacheManager(
                redisTemplate());
        redisCacheManager.setTransactionAware(true);
        // redisCacheManager.setDefaultExpiration(10); /*Enabled for Testing Purposes*/
        return redisCacheManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#errorHandler()
     */
    /** {@inheritDoc} */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

}
