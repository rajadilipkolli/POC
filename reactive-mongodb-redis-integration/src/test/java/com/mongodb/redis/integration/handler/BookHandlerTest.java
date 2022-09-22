package com.mongodb.redis.integration.handler;

import com.mongodb.redis.integration.request.BookDTO;
import com.mongodb.redis.integration.service.ReactiveCachingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookHandlerTest {

    @Mock
    private ReactiveCachingService reactiveCachingService;

    @InjectMocks
    private BookHandler bookHandler;

    @Test
    public void testGetAll() {
        when(reactiveCachingService.findAllBooks()).thenReturn(Flux.just(new BookDTO("1", "book1", "author1")));
        StepVerifier.create(bookHandler.getAll())
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    public void testGetBook() {
        when(reactiveCachingService.getBookById("1")).thenReturn(Mono.just(new BookDTO("1","book1", "author1","text",1L)));
        StepVerifier.create(bookHandler.getBook(MockServerRequest.builder().method(HttpMethod.GET).pathVariable("id", "1").build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    public void testGetBookNotFound() {
        when(reactiveCachingService.getBookById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.getBook(MockServerRequest.builder().method(HttpMethod.GET).pathVariable("id", "1").build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    public void testDeleteBook() {
        when(reactiveCachingService.deleteBook(anyString())).thenReturn(Mono.just(new BookDTO("1", "book1", "author1")));
        StepVerifier.create(bookHandler.deleteBook(MockServerRequest.builder().method(HttpMethod.DELETE).pathVariable("id", "1").build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.ACCEPTED))
                .verifyComplete();
    }

    @Test
    public void testDeleteBookNotFound() {
        when(reactiveCachingService.deleteBook(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.deleteBook(MockServerRequest.builder().method(HttpMethod.DELETE).pathVariable("id", "1").build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    public void testDeleteAllBooks() {
        when(reactiveCachingService.deleteAll()).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.deleteAllBooks())
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.ACCEPTED))
                .verifyComplete();
    }
}
