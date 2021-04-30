package com.mongodb.redis.integration.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ItemReactiveRepository;
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

  @MockBean private ItemReactiveRepository itemReactiveRepository;

  @Test
  void getAllItems() {

    given(this.itemReactiveRepository.findAll())
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

    given(this.itemReactiveRepository.findAll())
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

    given(this.itemReactiveRepository.findById("ABC"))
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

    given(this.itemReactiveRepository.findById("ABC")).willReturn(Mono.empty());

    this.webTestClient
        .get()
        .uri(ItemConstants.ITEM_END_POINT_V_1.concat("/{id}"), "ABC")
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .isEmpty();
  }
}
