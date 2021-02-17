package com.mongodb.redis.integration.utils;

import com.mongodb.redis.integration.document.Book;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class FunctionalEndpointUtils {
  public static String id(ServerRequest serverRequest) {
    // parse id from path-variable
    return serverRequest.pathVariable("id");
  }

  public static Mono<ServerResponse> defaultReadResponse(Publisher<Book> books) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(books, Book.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }
}
