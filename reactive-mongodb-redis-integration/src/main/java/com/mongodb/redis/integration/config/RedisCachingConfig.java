package com.mongodb.redis.integration.config;

import com.mongodb.redis.integration.document.Book;
import javax.annotation.PreDestroy;
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

    Jackson2JsonRedisSerializer<Book> serializer = new Jackson2JsonRedisSerializer<>(Book.class);

    RedisSerializationContext<String, Book> serializationContext =
        RedisSerializationContext.<String, Book>newSerializationContext(new StringRedisSerializer())
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
