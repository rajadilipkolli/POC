package com.mongodb.redis.integration.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.BookReactiveRepository;
import com.mongodb.redis.integration.utils.MockObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ReactiveBookServiceTest {

    @Mock private BookReactiveRepository bookReactiveRepository;

    @Mock private ApplicationEventPublisher publisher;

    @Mock private ReactiveRedisTemplate<String, Book> reactiveJsonBookRedisTemplate;

    @InjectMocks private ReactiveBookService reactiveBookService;

    private final Book dummyBook =
            Book.builder()
                    .title("JUNIT_TITLE")
                    .author("JUNIT_AUTHOR")
                    .bookId("JUNIT")
                    .text("JUNIT_TEXT")
                    .version(1L)
                    .build();

    @Test
    void getBookDetailsReturnBookInfo() {

        given(this.bookReactiveRepository.findByTitle(eq("JUNIT_TITLE")))
                .willReturn(Mono.just(dummyBook));

        Mono<Book> book = this.reactiveBookService.findBookByTitle("JUNIT_TITLE");

        StepVerifier.create(book)
                .expectNextMatches(
                        foundBook ->
                                foundBook.getBookId().equals("JUNIT")
                                        && foundBook.getTitle().equalsIgnoreCase("JUNIT_TITLE")
                                        && foundBook.getAuthor().equalsIgnoreCase("JUNIT_AUTHOR")
                                        && foundBook.getText().equals("JUNIT_TEXT")
                                        && foundBook.getVersion().equals(1L))
                .expectComplete()
                .verify();
    }

    @Test
    void getBookDetailsWhenBookNotFound() {
        given(this.bookReactiveRepository.findByTitle(eq("prius"))).willReturn(Mono.empty());

        Mono<Book> errorBook = this.reactiveBookService.findBookByTitle("prius");

        StepVerifier.create(errorBook)
                .expectNextCount(0)
                .expectErrorMatches(
                        throwable ->
                                throwable instanceof BookNotFoundException
                                        && throwable
                                                .getMessage()
                                                .equals("Book with Title prius NotFound!"))
                .verify();
    }

    @Test
    void getAllBookDetails() {
        ReflectionTestUtils.setField(
                reactiveBookService,
                "bookReactiveHashOperations",
                MockObjectUtils.getMockHashOps());
        given(this.bookReactiveRepository.findAll()).willReturn(Flux.just(dummyBook));

        StepVerifier.create(this.reactiveBookService.findAllBooks())
                .expectNextMatches(
                        foundBook ->
                                foundBook.getBookId().equals("JUNIT")
                                        && foundBook.getTitle().equalsIgnoreCase("JUNIT_TITLE")
                                        && foundBook.getAuthor().equalsIgnoreCase("JUNIT_AUTHOR")
                                        && foundBook.getText().equals("JUNIT_TEXT")
                                        && foundBook.getVersion().equals(1L))
                .expectComplete()
                .verify();
    }
}
