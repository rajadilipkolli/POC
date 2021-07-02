package com.mongodb.redis.integration.utils;

import com.mongodb.redis.integration.request.BookDTO;
import lombok.experimental.UtilityClass;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class FunctionalEndpointUtils {

    public static String id(ServerRequest serverRequest) {
        // parse id from path-variable
        return serverRequest.pathVariable("id");
    }

    public static Mono<ServerResponse> defaultReadResponse(Publisher<BookDTO> books) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(books, BookDTO.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
