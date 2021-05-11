package com.mongodb.redis.integration.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import com.mongodb.redis.integration.constants.ItemConstants;
import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ItemReactiveRepository;
import com.mongodb.redis.integration.utils.FunctionalEndpointUtils;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ItemsHandler {

  private final ItemReactiveRepository itemReactiveRepository;

  // build notFound response
  private static final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

  public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.itemReactiveRepository.findAll(), Item.class);
  }

  public Mono<ServerResponse> getItemById(ServerRequest serverRequest) {
    String id = FunctionalEndpointUtils.id(serverRequest);
    Mono<Item> itemMono = this.itemReactiveRepository.findById(id);
    // build response
    return itemMono
        .flatMap(
            item ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(item)))
        .switchIfEmpty(notFound);
  }

  public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
    Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class);
    return itemToBeInserted
        .flatMap(this.itemReactiveRepository::save)
        .flatMap(
            item ->
                ServerResponse.created(
                        URI.create(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V_1 + item.getId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(item), Item.class));
  }

  public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
    String id = FunctionalEndpointUtils.id(serverRequest);
    return ServerResponse.accepted()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.itemReactiveRepository.deleteById(id), Book.class)
        .switchIfEmpty(notFound);
  }

  public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
    String id = FunctionalEndpointUtils.id(serverRequest);
    Mono<Item> updatedItem =
        serverRequest
            .bodyToMono(Item.class)
            .flatMap(
                item -> {
                  return itemReactiveRepository
                      .findById(id)
                      .flatMap(
                          currentItem -> {
                            currentItem.setPrice(item.getPrice());
                            currentItem.setDescription(item.getDescription());
                            return this.itemReactiveRepository.save(currentItem);
                          });
                });
    return updatedItem
        .flatMap(
            item ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(item)))
        .switchIfEmpty(notFound);
  }
}
