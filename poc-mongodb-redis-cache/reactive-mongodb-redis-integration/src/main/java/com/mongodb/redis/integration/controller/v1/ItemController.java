/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.controller.v1;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ReactiveItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ReactiveItemRepository reactiveItemRepository;

    @GetMapping(ItemConstants.ITEM_END_POINT_V_1)
    public Flux<Item> getAllItems() {
        return reactiveItemRepository.findAll();
    }

    @GetMapping(ItemConstants.ITEM_END_POINT_V_1 + "/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
        return reactiveItemRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstants.ITEM_END_POINT_V_1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> postItem(@RequestBody Item item) {
        return reactiveItemRepository.save(item);
    }

    @PutMapping(ItemConstants.ITEM_END_POINT_V_1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@RequestBody Item item, @PathVariable String id) {
        return reactiveItemRepository
                .findById(id)
                .flatMap(
                        currentItem -> {
                            currentItem.setPrice(item.getPrice());
                            currentItem.setDescription(item.getDescription());
                            return reactiveItemRepository.save(currentItem);
                        })
                .map(updateItem -> new ResponseEntity<>(updateItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(ItemConstants.ITEM_END_POINT_V_1 + "/{id}")
    public Mono<Void> deleteItemById(@PathVariable String id) {
        return reactiveItemRepository.deleteById(id);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTimeException(RuntimeException ex) {
        log.error("Exception caught in handleRunTimeException() ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
