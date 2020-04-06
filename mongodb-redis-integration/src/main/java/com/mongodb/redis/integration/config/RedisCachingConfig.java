/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mongodb.redis.integration.config;

import javax.annotation.PreDestroy;

import com.mongodb.redis.integration.document.Book;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Cache Configurer.
 *
 * @author Raja Kolli
 *
 */
@Configuration(proxyBeanMethods = false)
@EnableCaching
@RequiredArgsConstructor
public class RedisCachingConfig extends CachingConfigurerSupport {

	private final RedisConnectionFactory connectionFactory;

	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		return new CustomCacheErrorHandler();
	}

	@Bean
	public ReactiveRedisTemplate<String, Book> reactiveJsonBookRedisTemplate(
			ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {

		RedisSerializationContext<String, Book> serializationContext = RedisSerializationContext
				.<String, Book>newSerializationContext(new StringRedisSerializer()).hashKey(new StringRedisSerializer())
				.hashValue(new Jackson2JsonRedisSerializer<>(Book.class)).build();

		return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
	}

	@PreDestroy
	public void flushTestDb() {
		this.connectionFactory.getConnection().flushDb();
	}

}
