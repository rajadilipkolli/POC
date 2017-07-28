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

/**
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 *
 */
@EnableCaching
@Configuration
public class RedisConfiguration extends CachingConfigurerSupport {

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
