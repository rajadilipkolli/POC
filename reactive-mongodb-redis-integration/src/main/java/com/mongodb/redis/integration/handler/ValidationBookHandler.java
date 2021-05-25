package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.request.BookDTO;
import com.mongodb.redis.integration.route.AbstractValidationHandler;
import com.mongodb.redis.integration.service.ReactiveCachingService;
import com.mongodb.redis.integration.utils.FunctionalEndpointUtils;
import java.net.URI;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ValidationBookHandler extends AbstractValidationHandler<BookDTO, Validator> {

    private final ReactiveCachingService reactiveCachingService;

    protected ValidationBookHandler(
            @Autowired Validator validator, ReactiveCachingService reactiveCachingService) {
        super(BookDTO.class, validator);
        this.reactiveCachingService = reactiveCachingService;
    }

    @Override
    protected Mono<ServerResponse> processBody(BookDTO validBody, ServerRequest originalRequest) {
        if (originalRequest.method() == HttpMethod.POST) {
            Flux<BookDTO> flux =
                    Flux.just(validBody).flatMap(this.reactiveCachingService::createBook);
            return defaultWriteResponse(flux);
        } else {
            return FunctionalEndpointUtils.defaultReadResponse(
                    Flux.just(validBody)
                            .flatMap(
                                    validatedBook ->
                                            this.reactiveCachingService.updateBook(
                                                    FunctionalEndpointUtils.id(originalRequest),
                                                    validatedBook)));
        }
    }

    private Mono<ServerResponse> defaultWriteResponse(Publisher<BookDTO> books) {
        return Mono.from(books)
                .flatMap(
                        book ->
                                ServerResponse.created(URI.create("/api/book/" + book.getBookId()))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .build());
    }
}
