package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.request.BookDTO;
import com.mongodb.redis.integration.service.ReactiveCachingService;
import com.mongodb.redis.integration.utils.FunctionalEndpointUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final ReactiveCachingService reactiveCachingService;

    // build notFound response
    private static final Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAll() {
        return FunctionalEndpointUtils.defaultReadResponse(
                this.reactiveCachingService.findAllBooks());
    }

    public Mono<ServerResponse> getBook(ServerRequest request) {
        // get book from repository
        Mono<BookDTO> bookDTOMono =
                this.reactiveCachingService.getBookById(FunctionalEndpointUtils.id(request));

        // build response
        return bookDTOMono
                .flatMap(
                        bookDTO ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(bookDTO)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteBook(ServerRequest request) {
        Mono<BookDTO> bookDTOMono =
                this.reactiveCachingService.deleteBook(FunctionalEndpointUtils.id(request));
        return bookDTOMono
                .flatMap(
                        bookDTO ->
                                ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .build())
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAllBooks() {
        return ServerResponse.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.reactiveCachingService.deleteAll(), Void.class);
    }
}
