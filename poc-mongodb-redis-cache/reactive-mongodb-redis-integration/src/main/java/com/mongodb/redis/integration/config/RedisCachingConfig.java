/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.config;

import com.mongodb.redis.integration.request.BookDTO;

import jakarta.annotation.PreDestroy;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CachingConfigurer;
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

@Configuration(proxyBeanMethods = false)
@EnableCaching
@RequiredArgsConstructor
public class RedisCachingConfig implements CachingConfigurer {

    private final RedisConnectionFactory connectionFactory;

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    @Bean
    public ReactiveRedisTemplate<String, BookDTO> reactiveJsonBookRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {

        Jackson2JsonRedisSerializer<BookDTO> serializer =
                new Jackson2JsonRedisSerializer<>(BookDTO.class);

        RedisSerializationContext<String, BookDTO> serializationContext =
                RedisSerializationContext.<String, BookDTO>newSerializationContext(
                                new StringRedisSerializer())
                        .hashKey(new StringRedisSerializer())
                        .hashValue(serializer)
                        .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

    @PreDestroy
    public void flushTestDb() {
        this.connectionFactory.getConnection().flushDb();
    }
}
