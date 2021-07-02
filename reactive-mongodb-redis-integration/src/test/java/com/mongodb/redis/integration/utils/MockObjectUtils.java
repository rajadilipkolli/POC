package com.mongodb.redis.integration.utils;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.document.Item;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ScanOptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MockObjectUtils {

    public static ReactiveHashOperations<String, String, Book> getMockHashOps() {
        return new ReactiveHashOperations<>() {
            @Override
            public Mono<Long> remove(String key, Object... hashKeys) {
                return null;
            }

            @Override
            public Mono<Boolean> hasKey(String key, Object hashKey) {
                return null;
            }

            @Override
            public Mono<Book> get(String key, Object hashKey) {
                return null;
            }

            @Override
            public Mono<List<Book>> multiGet(String key, Collection<String> hashKeys) {
                return null;
            }

            @Override
            public Mono<Long> increment(String key, String hashKey, long delta) {
                return null;
            }

            @Override
            public Mono<Double> increment(String key, String hashKey, double delta) {
                return null;
            }

            @Override
            public Flux<String> keys(String key) {
                return null;
            }

            @Override
            public Mono<Long> size(String key) {
                return null;
            }

            @Override
            public Mono<Boolean> putAll(String key, Map<? extends String, ? extends Book> map) {
                return null;
            }

            @Override
            public Mono<Boolean> put(String key, String hashKey, Book value) {
                return Mono.just(Boolean.TRUE);
            }

            @Override
            public Mono<Boolean> putIfAbsent(String key, String hashKey, Book value) {
                return null;
            }

            @Override
            public Flux<Book> values(String key) {
                return Flux.empty();
            }

            @Override
            public Flux<Map.Entry<String, Book>> entries(String key) {
                return null;
            }

            @Override
            public Flux<Map.Entry<String, Book>> scan(String key, ScanOptions options) {
                return null;
            }

            @Override
            public Mono<Boolean> delete(String key) {
                return null;
            }
        };
    }

    public static List<Item> getItemsList() {
        return List.of(
                new Item(null, "Samsung TV", 40000.0),
                new Item(null, "LG TV", 42000.0),
                new Item(null, "Apple Watch", 90000.99),
                getItemById("ABC"));
    }

    public static Item getItemById(String id) {
        return new Item(id, "Bose Headphone", 900.99);
    }
}
