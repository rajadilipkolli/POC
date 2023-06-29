/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ReactiveItemRepository;
import com.mongodb.redis.integration.utils.MockObjectUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WebFluxTest(controllers = ItemController.class)
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired private WebTestClient webTestClient;

    @MockBean private ReactiveItemRepository reactiveItemRepository;

    @Test
    void getAllItems() {

        given(this.reactiveItemRepository.findAll())
                .willReturn(Flux.fromIterable(MockObjectUtils.getItemsList()));

        this.webTestClient
                .get()
                .uri(ItemConstants.ITEM_END_POINT_V_1)
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
                            assertThat(items).isNotEmpty();
                            items.forEach(item -> assertThat(item.getDescription()).isNotNull());
                        });
    }

    @Test
    void getAllItems_approach2() {

        given(this.reactiveItemRepository.findAll())
                .willReturn(Flux.fromIterable(MockObjectUtils.getItemsList()));

        Flux<Item> itemsFlux =
                this.webTestClient
                        .get()
                        .uri(ItemConstants.ITEM_END_POINT_V_1)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_JSON)
                        .returnResult(Item.class)
                        .getResponseBody();

        StepVerifier.create(itemsFlux.log("approach 2"))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getOneItemSuccess() {

        given(this.reactiveItemRepository.findById("ABC"))
                .willReturn(Mono.just(MockObjectUtils.getItemById("ABC")));

        this.webTestClient
                .get()
                .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.price", "900.99");
    }

    @Test
    void getOneItemNotFound() {

        given(this.reactiveItemRepository.findById("BCD")).willReturn(Mono.empty());

        this.webTestClient
                .get()
                .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "BCD")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .isEmpty();
    }

    @Test
    void saveItem() {

        Item item = MockObjectUtils.getItemById("ABC");
        given(this.reactiveItemRepository.save(item)).willReturn(Mono.just(item));

        this.webTestClient
                .post()
                .uri(ItemConstants.ITEM_END_POINT_V_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.price", item.getPrice());
    }

    @Test
    void updateItem() {
        double newPrice = 129.95;

        Item item = MockObjectUtils.getItemById("ABC");
        item.setPrice(newPrice);

        given(this.reactiveItemRepository.findById("ABC"))
                .willReturn(Mono.just(MockObjectUtils.getItemById("ABC")));

        given(this.reactiveItemRepository.save(item)).willReturn(Mono.just(item));

        this.webTestClient
                .put()
                .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "ABC")
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
    void updateItem_is_not_found() {
        double newPrice = 129.95;

        Item item = MockObjectUtils.getItemById("DEF");
        item.setPrice(newPrice);

        given(this.reactiveItemRepository.findById("DEF")).willReturn(Mono.empty());
        this.webTestClient
                .put()
                .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "DEF")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteItem() {
        this.webTestClient
                .delete()
                .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);
    }
}
