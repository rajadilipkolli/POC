/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.config.TestContainersConfig;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.utils.MockObjectUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Import(TestContainersConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReactiveItemRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ReactiveItemRepositoryTest.class);
    @Autowired private ReactiveItemRepository reactiveItemRepository;

    @BeforeAll
    void setUp() {
        reactiveItemRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(MockObjectUtils.getItemsList()))
                .flatMap(reactiveItemRepository::save)
                .doOnNext(item -> log.info("Inserted Item is : {}", item))
                .blockLast();
    }

    @Test
    void test01_getAllItems() {
        Flux<Item> allItems = reactiveItemRepository.findAll();

        StepVerifier.create(allItems.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void test02_getItemById() {
        Mono<Item> itemMono = this.reactiveItemRepository.findById("ABC");

        StepVerifier.create(itemMono.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphone"))
                .verifyComplete();
    }

    @Test
    void test03_findItemByDescription() {
        StepVerifier.create(
                        reactiveItemRepository
                                .findByDescription("Bose Headphone")
                                .log("findByDescription"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void test04_saveItem() {
        Item item = new Item(null, "Home Mini", 30.0);
        StepVerifier.create(reactiveItemRepository.save(item).log("saveItem"))
                .expectSubscription()
                .expectNextMatches(
                        item1 ->
                                (item1.getId() != null
                                        && item1.getDescription().equals("Home Mini")
                                        && item1.getPrice().intValue() == 30))
                .verifyComplete();
    }

    @Test
    void test05_updateItem() {
        double newPrice = 520.99;
        Flux<Item> updatedItem =
                this.reactiveItemRepository
                        .findByDescription("Samsung TV")
                        .map(
                                item -> {
                                    item.setPrice(newPrice);
                                    return item;
                                })
                        .flatMap(item -> reactiveItemRepository.save(item));

        StepVerifier.create(updatedItem.log("update item"))
                .expectSubscription()
                .expectNextMatches(item -> (item.getPrice() == 520.99))
                .verifyComplete();
    }

    @Test
    void test06_deleteItem() {
        Mono<Void> deletedItem =
                reactiveItemRepository
                        .findById("ABC")
                        .map(Item::getId)
                        .flatMap(itemId -> reactiveItemRepository.deleteById(itemId));

        StepVerifier.create(deletedItem.log("Delete Item")).expectSubscription().verifyComplete();

        StepVerifier.create(reactiveItemRepository.findById("ABC"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
