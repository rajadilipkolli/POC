package com.poc.mongodbredisintegration.config;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@EnableCaching
@Configuration
public class RedisConfiguration extends CachingConfigurerSupport
        implements CachingConfigurer {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    /* redis template definition */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
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
    @Bean
    @Override
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        redisCacheManager.setTransactionAware(true);
        // redisCacheManager.setDefaultExpiration(10); /*Enabled for Testing Purposes*/
        return redisCacheManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.cache.annotation.CachingConfigurerSupport#errorHandler()
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

}
