/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.controller.v1;

import com.mongodb.redis.integration.config.AbstractIntegrationTest;
import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.ItemCapped;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ItemStreamControllerIntegrationTest extends AbstractIntegrationTest {

    private static final Logger log =
            LoggerFactory.getLogger(ItemStreamControllerIntegrationTest.class);

    @BeforeAll
    void setUp() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(
                ItemCapped.class, CollectionOptions.empty().maxDocuments(50).size(5000).capped());
        Flux<ItemCapped> itemCappedFlux =
                Flux.range(0, 5).map(i -> new ItemCapped(null, "Random Item " + i, 100.00 + i));

        reactiveItemCappedRepository
                .insert(itemCappedFlux)
                .doOnNext(itemCapped -> log.info("ItemCapped inserted is :{}", itemCapped))
                .blockLast();
    }

    @Test
    void testStreamingAllItems() {
        Flux<ItemCapped> itemCappedFlux =
                webTestClient
                        .get()
                        .uri(ItemConstants.ITEM_STREAM_END_POINT_V_1)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectHeader()
                        .contentType(MediaType.APPLICATION_NDJSON)
                        .returnResult(ItemCapped.class)
                        .getResponseBody()
                        .take(5);

        StepVerifier.create(itemCappedFlux).expectNextCount(5).thenCancel().verify();
    }
}
