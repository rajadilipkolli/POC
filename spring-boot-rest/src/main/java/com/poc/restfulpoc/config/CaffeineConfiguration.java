/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.restfulpoc.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * CaffeineConfiguration class.
 * </p>
 *
 * @author Raja Kolli
 * @since July 2017
 * @version 0 : 5
 */
@EnableCaching
@Configuration
public class CaffeineConfiguration extends CachingConfigurerSupport {

	/** {@inheritDoc} */
	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		return new CustomCacheErrorHandler();
	}

}
