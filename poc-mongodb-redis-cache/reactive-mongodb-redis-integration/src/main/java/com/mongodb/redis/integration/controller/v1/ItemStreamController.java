/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.controller.v1;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.ItemCapped;
import com.mongodb.redis.integration.repository.ReactiveItemCappedRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ItemStreamController {

    private final ReactiveItemCappedRepository reactiveItemCappedRepository;

    public ItemStreamController(ReactiveItemCappedRepository reactiveItemCappedRepository) {
        this.reactiveItemCappedRepository = reactiveItemCappedRepository;
    }

    @GetMapping(
            value = ItemConstants.ITEM_STREAM_END_POINT_V_1,
            produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ItemCapped> getItems() {

        return reactiveItemCappedRepository.findItemsBy();
    }
}
