/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.initialize;

import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.document.ItemCapped;
import com.mongodb.redis.integration.repository.ReactiveItemCappedRepository;
import com.mongodb.redis.integration.repository.ReactiveItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class ItemDataInitiliazer implements CommandLineRunner {

    private final ReactiveItemRepository reactiveItemRepository;
    private final ReactiveItemCappedRepository reactiveItemCappedRepository;
    private final MongoOperations mongoOperations;

    @Override
    public void run(String... args) {
        initialDataSetUp();
        createCappedCollection();
        dataSetUpForCappedCollection();
    }

    private void createCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class);
        CollectionOptions collectionOptions =
                CollectionOptions.empty().maxDocuments(20).size(5000).capped();
        mongoOperations.createCollection(ItemCapped.class, collectionOptions);
    }

    private void dataSetUpForCappedCollection() {
        Flux<ItemCapped> itemCappedFlux =
                Flux.interval(Duration.ofSeconds(1))
                        .map(i -> new ItemCapped(null, "Random Item " + i, 100.00 + i));

        reactiveItemCappedRepository
                .insert(itemCappedFlux)
                .subscribe(itemCapped -> log.info("ItemCapped inserted is :{}", itemCapped));
    }

    private void initialDataSetUp() {
        this.reactiveItemRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(getItemsList()))
                .flatMap(this.reactiveItemRepository::save)
                .thenMany(this.reactiveItemRepository.findAll())
                .subscribe(item -> log.info("Items Inserted from CommandlineRunner : {}", item));
    }

    public static List<Item> getItemsList() {
        return List.of(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 4200.0),
                new Item(null, "Apple Watch", 9000.99),
                new Item(null, "Bose Headphone", 900.99));
    }
}
