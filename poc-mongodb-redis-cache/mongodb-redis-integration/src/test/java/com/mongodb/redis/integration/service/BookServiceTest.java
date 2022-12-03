/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.BookRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BookServiceTest {

    @Mock private BookRepository bookRepository;

    @InjectMocks private BookService bookService;

    @Test
    void getBookDetailsReturnBookInfo() throws BookNotFoundException {
        Book builderBook =
                Book.builder()
                        .title("JUNIT_TITLE")
                        .author("JUNIT_AUTHOR")
                        .bookId("JUNIT")
                        .text("JUNIT_TEXT")
                        .version(1L)
                        .build();
        given(this.bookRepository.findBookByTitle(eq("JUNIT_TITLE")))
                .willReturn(Optional.of(builderBook));

        Book book = this.bookService.findBookByTitle("JUNIT_TITLE");

        assertThat(book.getTitle()).isEqualTo("JUNIT_TITLE");
        assertThat(book.getAuthor()).isEqualTo("JUNIT_AUTHOR");
        assertThat(book.getText()).isEqualTo("JUNIT_TEXT");
    }

    @Test
    void getBookDetailsWhenBookNotFound() {
        given(this.bookRepository.findBookByTitle(eq("prius"))).willReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> this.bookService.findBookByTitle("prius"));

        assertThat(throwable)
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with Title prius NotFound!");
    }
}
