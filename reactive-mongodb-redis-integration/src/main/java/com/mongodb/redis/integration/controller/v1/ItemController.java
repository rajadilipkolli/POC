package com.mongodb.redis.integration.controller.v1;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemReactiveRepository itemReactiveRepository;

  @GetMapping(ItemConstants.ITEM_END_POINT_V_1)
  public Flux<Item> getAllItems() {
    return itemReactiveRepository.findAll();
  }

  @GetMapping(ItemConstants.ITEM_END_POINT_V_1 + "/{id}")
  public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
    return itemReactiveRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping(ItemConstants.ITEM_END_POINT_V_1)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Item> postItem(@RequestBody Item item) {
    return itemReactiveRepository.save(item);
  }
}
