package com.example.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Protocol;

@Configuration
public class RedisConfiguration extends CachingConfigurerSupport
{
    /* Jedis ConnectionFactory */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory()
    {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(
                Protocol.DEFAULT_HOST, Protocol.DEFAULT_PORT);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(
                standaloneConfig);
        return jedisConnectionFactory;
    }

    /* redis template definition */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate()
    {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setExposeConnection(true);
        /*
        redisTemplate.setKeySerializer( new StringRedisSerializer() );
        redisTemplate.setHashValueSerializer( new GenericToStringSerializer< Book >( Book.class ) ); 
        redisTemplate.setValueSerializer( new GenericToStringSerializer< Book >( Book.class ) );
         */
        return redisTemplate;
    }

    /* declare Redis Cache Manager */
    @Bean
    @Override
    public RedisCacheManager cacheManager()
    {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
        redisCacheManager.setTransactionAware(true);
        redisCacheManager.setLoadRemoteCachesOnStartup(true);
//        redisCacheManager.setDefaultExpiration(10); /*Enabled for Testing Purposes*/
        redisCacheManager.setUsePrefix(true);
        return redisCacheManager;
    }

}
