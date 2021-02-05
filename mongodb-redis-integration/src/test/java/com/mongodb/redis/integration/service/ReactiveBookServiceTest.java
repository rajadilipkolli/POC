package com.mongodb.redis.integration.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ReactiveBookServiceTest {

  @Mock private ReactiveBookRepository reactiveBookRepository;

  @InjectMocks private ReactiveBookService reactiveBookService;

  @Test
  void getBookDetailsReturnBookInfo() {
    Book builderBook =
        Book.builder()
            .title("JUNIT_TITLE")
            .author("JUNIT_AUTHOR")
            .bookId("JUNIT")
            .text("JUNIT_TEXT")
            .version(1L)
            .build();
    given(this.reactiveBookRepository.findByTitle(eq("JUNIT_TITLE")))
        .willReturn(Mono.just(builderBook));

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
    given(this.reactiveBookRepository.findByTitle(eq("prius"))).willReturn(Mono.empty());

    Mono<Book> book = this.reactiveBookService.findBookByTitle("prius");

    StepVerifier.create(book)
        .expectErrorMessage("Book with Title prius NotFound!")
        .verifyThenAssertThat()
        .hasNotDiscardedElements();
  }
}
