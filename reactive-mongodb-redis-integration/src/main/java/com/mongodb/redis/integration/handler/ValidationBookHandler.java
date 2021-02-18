package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.route.AbstractValidationHandler;
import com.mongodb.redis.integration.service.ReactiveBookService;
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
public class ValidationBookHandler extends AbstractValidationHandler<Book, Validator> {

  private final ReactiveBookService reactiveBookService;

  protected ValidationBookHandler(
      @Autowired Validator validator, ReactiveBookService reactiveBookService) {
    super(Book.class, validator);
    this.reactiveBookService = reactiveBookService;
  }

  @Override
  protected Mono<ServerResponse> processBody(Book validBody, ServerRequest originalRequest) {
    if (originalRequest.method() == HttpMethod.POST) {
      Flux<Book> flux = Flux.just(validBody).flatMap(this.reactiveBookService::createBook);
      return defaultWriteResponse(flux);
    } else {
      return FunctionalEndpointUtils.defaultReadResponse(
          Flux.just(validBody)
              .flatMap(
                  validatedBook ->
                      this.reactiveBookService.updateBook(
                          FunctionalEndpointUtils.id(originalRequest), validatedBook)));
    }
  }

  private Mono<ServerResponse> defaultWriteResponse(Publisher<Book> books) {
    return Mono.from(books)
        .flatMap(
            book ->
                ServerResponse.created(URI.create("/api/book/" + book.getBookId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .build());
  }
}
