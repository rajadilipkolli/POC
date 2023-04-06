/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.handler;

import static com.mongodb.redis.integration.constants.ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1;

import static org.assertj.core.api.Assertions.assertThat;

import com.mongodb.redis.integration.config.AbstractIntegrationTest;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ReactiveItemRepository;
import com.mongodb.redis.integration.utils.MockObjectUtils;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
class ItemsHandlerTest extends AbstractIntegrationTest {

    @Autowired private ReactiveItemRepository reactiveItemRepository;

    @BeforeEach
    void setUp() {
        this.reactiveItemRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(MockObjectUtils.getItemsList()))
                .flatMap(reactiveItemRepository::save)
                .doOnNext(
                        item -> {
                            log.info("Inserted Record :{}", item);
                        })
                .blockLast();
    }

    @Test
    void getAllItems() {

        this.webTestClient
                .get()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(
                        response -> {
                            List<Item> items = response.getResponseBody();
                            assertThat(items).isNotEmpty().hasSize(4);
                            items.forEach(item -> assertThat(item.getId()).isNotNull());
                        });
    }

    @Test
    void testGetAllItems_approach2() {

        Flux<Item> itemsFlux =
                webTestClient
                        .get()
                        .uri(ITEM_FUNCTIONAL_END_POINT_V_1)
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .returnResult(Item.class)
                        .getResponseBody();

        this.reactiveItemRepository
                .count()
                .flatMap(
                        count -> {
                            StepVerifier.create(itemsFlux).expectNextCount(count).verifyComplete();
                            return null;
                        });
    }

    @Test
    void getOneItemSuccess() {

        this.webTestClient
                .get()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.price", "900.99");
    }

    @Test
    void getOneItemNotFound() {

        this.webTestClient
                .get()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1.concat("/{id}"), "BCD")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .isEmpty();
    }

    @Test
    void testCreateItem() {

        Item item = new Item(null, "Iphone X", 999.99);

        webTestClient
                .post()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id")
                .isNotEmpty()
                .jsonPath("$.description")
                .isEqualTo("Iphone X")
                .jsonPath("$.price")
                .isEqualTo("999.99");
    }

    @Test
    void deleteItem() {
        this.webTestClient
                .delete()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Void.class);
    }

    @Test
    void testUpdateItem() {
        double newPrice = 129.99;
        Item item = new Item(null, "Beats HeadPhones", newPrice);
        webTestClient
                .put()
                .uri(ITEM_FUNCTIONAL_END_POINT_V_1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.price", newPrice);
    }

    @Test
    void testUpdateItem_notFound() {
        double newPrice = 129.99;
        Item item = new Item(null, "Beats HeadPhones", newPrice);
        webTestClient
                .put()
                .uri(
                        ITEM_FUNCTIONAL_END_POINT_V_1.concat("/{id}"),
                        "DEF") // no record with this ids
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
