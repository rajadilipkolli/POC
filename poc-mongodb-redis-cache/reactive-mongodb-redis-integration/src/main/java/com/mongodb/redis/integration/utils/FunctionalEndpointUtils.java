/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.utils;

import com.mongodb.redis.integration.request.BookDTO;
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

    public static Mono<ServerResponse> defaultReadResponse(Publisher<BookDTO> books) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(books, BookDTO.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
