package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.request.BookDTO;
import com.mongodb.redis.integration.service.ReactiveCachingService;
import com.mongodb.redis.integration.utils.FunctionalEndpointUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record BookHandler(ReactiveCachingService reactiveCachingService) {

    // build notFound response
    private static final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAll() {
        return FunctionalEndpointUtils.defaultReadResponse(
                this.reactiveCachingService.findAllBooks());
    }

    public Mono<ServerResponse> getBook(ServerRequest request) {
        // get book from repository
        Mono<BookDTO> bookMono =
                this.reactiveCachingService.getBookById(FunctionalEndpointUtils.id(request));

        // build response
        return bookMono.flatMap(
                        (BookDTO book) ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(book)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        return ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        this.reactiveCachingService.deleteBook(FunctionalEndpointUtils.id(request)),
                        Long.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllBooks() {
        return ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.reactiveCachingService.deleteAll(), Void.class);
    }
}
