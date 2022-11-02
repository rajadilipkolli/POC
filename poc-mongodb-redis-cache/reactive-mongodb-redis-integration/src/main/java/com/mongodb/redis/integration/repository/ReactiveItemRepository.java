package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ReactiveItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(String description);
}
