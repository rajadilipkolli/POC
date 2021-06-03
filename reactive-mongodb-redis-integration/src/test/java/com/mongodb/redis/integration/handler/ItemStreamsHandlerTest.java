package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.ItemCapped;
import com.mongodb.redis.integration.repository.ReactiveItemCappedRepository;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
class ItemStreamsHandlerTest {
    @Autowired private ReactiveItemCappedRepository reactiveItemCappedRepository;

    @Autowired private MongoOperations mongoOperations;

    @Autowired private WebTestClient webTestClient;

    @BeforeAll
    void setUp() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(
                ItemCapped.class, CollectionOptions.empty().maxDocuments(50).size(5000).capped());
        Flux<ItemCapped> itemCappedFlux =
                Flux.interval(Duration.ofMillis(5))
                        .map(i -> new ItemCapped(null, "Random Item " + i, 100.00 + i))
                        .take(5);

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
                        .uri(ItemConstants.ITEM_STREAM_FUNCTIONAL_END_POINT_V_1)
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
