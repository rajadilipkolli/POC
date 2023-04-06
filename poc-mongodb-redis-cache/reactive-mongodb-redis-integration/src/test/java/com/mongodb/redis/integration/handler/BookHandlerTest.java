/* Licensed under Apache-2.0 2022-2023 */
package com.mongodb.redis.integration.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.mongodb.redis.integration.request.BookDTO;
import com.mongodb.redis.integration.service.ReactiveCachingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BookHandlerTest {

    private BookHandler bookHandler;
    private ReactiveCachingService reactiveCachingService;

    @BeforeEach
    void setUp() {
        reactiveCachingService = Mockito.mock(ReactiveCachingService.class);
        bookHandler = new BookHandler(reactiveCachingService);
    }

    @Test
    void testGetAll() {
        when(reactiveCachingService.findAllBooks())
                .thenReturn(Flux.just(new BookDTO("1", "book1", "author1", "text1", 0L)));
        StepVerifier.create(bookHandler.getAll())
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testGetBook() {
        when(reactiveCachingService.getBookById("1"))
                .thenReturn(Mono.just(new BookDTO("1", "book1", "author1", "text", 1L)));
        StepVerifier.create(
                        bookHandler.getBook(
                                MockServerRequest.builder()
                                        .method(HttpMethod.GET)
                                        .pathVariable("id", "1")
                                        .build()))
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void testGetBookNotFound() {
        when(reactiveCachingService.getBookById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(
                        bookHandler.getBook(
                                MockServerRequest.builder()
                                        .method(HttpMethod.GET)
                                        .pathVariable("id", "1")
                                        .build()))
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testDeleteBook() {
        when(reactiveCachingService.deleteBook(anyString()))
                .thenReturn(Mono.just(new BookDTO("1", "book1", "author1", "text1", 0L)));
        StepVerifier.create(
                        bookHandler.deleteBook(
                                MockServerRequest.builder()
                                        .method(HttpMethod.DELETE)
                                        .pathVariable("id", "1")
                                        .build()))
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.ACCEPTED))
                .verifyComplete();
    }

    @Test
    void testDeleteBookNotFound() {
        when(reactiveCachingService.deleteBook(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(
                        bookHandler.deleteBook(
                                MockServerRequest.builder()
                                        .method(HttpMethod.DELETE)
                                        .pathVariable("id", "1")
                                        .build()))
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    void testDeleteAllBooks() {
        when(reactiveCachingService.deleteAll()).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.deleteAllBooks())
                .expectNextMatches(
                        serverResponse -> serverResponse.statusCode().equals(HttpStatus.ACCEPTED))
                .verifyComplete();
    }
}
