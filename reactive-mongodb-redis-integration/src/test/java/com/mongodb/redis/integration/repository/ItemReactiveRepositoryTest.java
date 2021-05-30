package com.mongodb.redis.integration.repository;

import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.utils.MockObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@TestMethodOrder(MethodOrderer.MethodName.class)
@Slf4j
class ItemReactiveRepositoryTest {

    @Autowired private ItemReactiveRepository itemReactiveRepository;

    @BeforeAll
    void setUp() {
        itemReactiveRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(MockObjectUtils.getItemsList()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> log.info("Inserted Item is : {}", item))
                .blockLast();
    }

    @Test
    void test01_getAllItems() {
        Flux<Item> allItems = itemReactiveRepository.findAll();

        StepVerifier.create(allItems.log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void test02_getItemById() {
        Mono<Item> itemMono = this.itemReactiveRepository.findById("ABC");

        StepVerifier.create(itemMono.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphone"))
                .verifyComplete();
    }

    @Test
    void test03_findItemByDescription() {
        StepVerifier.create(
                        itemReactiveRepository
                                .findByDescription("Bose Headphone")
                                .log("findByDescription"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void test04_saveItem() {
        Item item = new Item(null, "Home Mini", 30.0);
        StepVerifier.create(itemReactiveRepository.save(item).log("saveItem"))
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
                this.itemReactiveRepository
                        .findByDescription("Samsung TV")
                        .map(
                                item -> {
                                    item.setPrice(newPrice);
                                    return item;
                                })
                        .flatMap(item -> itemReactiveRepository.save(item));

        StepVerifier.create(updatedItem.log("update item"))
                .expectSubscription()
                .expectNextMatches(item -> (item.getPrice() == 520.99))
                .verifyComplete();
    }

    @Test
    void test06_deleteItem() {
        Mono<Void> deletedItem =
                itemReactiveRepository
                        .findById("ABC")
                        .map(Item::getId)
                        .flatMap(itemId -> itemReactiveRepository.deleteById(itemId));

        StepVerifier.create(deletedItem.log("Delete Item")).expectSubscription().verifyComplete();

        StepVerifier.create(itemReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}
