package com.mongodb.redis.integration.config;

import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

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

  @PreDestroy
  public void flushTestDb() {
    this.connectionFactory.getConnection().flushDb();
  }
}
