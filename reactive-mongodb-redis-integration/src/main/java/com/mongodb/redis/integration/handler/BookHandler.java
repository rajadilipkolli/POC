package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.service.ReactiveBookService;
import java.net.URI;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public record BookHandler(ReactiveBookService reactiveBookService) {

  public Mono<ServerResponse> getAll() {
    return defaultReadResponse(this.reactiveBookService.findAllBooks());
  }

  public Mono<ServerResponse> getBook(ServerRequest request) {
    // build notFound response
    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    // get book from repository
    Mono<Book> bookMono = this.reactiveBookService.getBookById(id(request));

    // build response
    return bookMono
        .flatMap(
            (Book book) ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(book)))
        .switchIfEmpty(notFound);
  }

  public Mono<ServerResponse> postBook(ServerRequest request) {
    Flux<Book> flux = request.bodyToFlux(Book.class).flatMap(this.reactiveBookService::createBook);
    return defaultWriteResponse(flux);
  }

  public Mono<ServerResponse> putBook(ServerRequest request) {

    Flux<Book> id =
        request
            .bodyToFlux(Book.class)
            .flatMap(p -> this.reactiveBookService.updateBook(id(request), p));
    return defaultReadResponse(id);
  }

  public Mono<ServerResponse> deleteBook(ServerRequest request) {
    return ServerResponse.accepted()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.reactiveBookService.deleteBook(id(request)), Book.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteAllBooks() {
    return ServerResponse.accepted()
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.reactiveBookService.deleteAll(), Void.class)
            .switchIfEmpty(ServerResponse.notFound().build());
  }

  private static Mono<ServerResponse> defaultWriteResponse(Publisher<Book> books) {
    return Mono.from(books)
        .flatMap(
            book ->
                ServerResponse.created(URI.create("/api/book/" + book.getBookId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .build());
  }

  private Mono<ServerResponse> defaultReadResponse(Publisher<Book> books) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(books, Book.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private static String id(ServerRequest serverRequest) {
    // parse id from path-variable
    return serverRequest.pathVariable("id");
  }
}
