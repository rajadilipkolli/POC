package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.service.ReactiveBookService;
import com.mongodb.redis.integration.utils.FunctionalEndpointUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record BookHandler(ReactiveBookService reactiveBookService) {

  // build notFound response
  static final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

  public Mono<ServerResponse> getAll() {
    return FunctionalEndpointUtils.defaultReadResponse(this.reactiveBookService.findAllBooks());
  }

  public Mono<ServerResponse> getBook(ServerRequest request) {
    // get book from repository
    Mono<Book> bookMono = this.reactiveBookService.getBookById(FunctionalEndpointUtils.id(request));

    // build response
    return bookMono
        .flatMap(
            (Book book) ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(book)))
        .switchIfEmpty(notFound);
  }

  public Mono<ServerResponse> deleteBook(ServerRequest request) {
    return ServerResponse.accepted()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.reactiveBookService.deleteBook(FunctionalEndpointUtils.id(request)), Book.class)
        .switchIfEmpty(notFound);
  }

  public Mono<ServerResponse> deleteAllBooks() {
    return ServerResponse.accepted()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.reactiveBookService.deleteAll(), Void.class)
        .switchIfEmpty(notFound);
  }
}
