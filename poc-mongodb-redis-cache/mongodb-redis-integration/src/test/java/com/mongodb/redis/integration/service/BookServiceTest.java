/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class BookServiceTest {

    @Mock private BookRepository bookRepository;

    @Mock private MongoTemplate mongoTemplate;

    @InjectMocks private BookService bookService;

    @Test
    void getBookDetailsReturnBookInfo() throws BookNotFoundException {
        String title = "JUNIT_TITLE";
        Book builderBook =
                new Book()
                        .setTitle("JUNIT_TITLE")
                        .setAuthor("JUNIT_AUTHOR")
                        .setBookId("JUNIT")
                        .setText("JUNIT_TEXT")
                        .setVersion(1L);
        given(this.bookRepository.findBookByTitle(eq(title))).willReturn(Optional.of(builderBook));

        Book book = this.bookService.findBookByTitle(title);

        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getAuthor()).isEqualTo("JUNIT_AUTHOR");
        assertThat(book.getText()).isEqualTo("JUNIT_TEXT");
        // Assert
        verify(bookRepository, times(1)).findBookByTitle(title);
    }

    @Test
    void getBookDetailsWhenBookNotFound() {
        given(this.bookRepository.findBookByTitle(eq("prius"))).willReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> this.bookService.findBookByTitle("prius"));

        assertThat(throwable)
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with Title prius NotFound!");
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book book =
                new Book()
                        .setBookId("1234567890")
                        .setTitle("Title")
                        .setAuthor("Author")
                        .setText("Text");

        // Mock the behavior of the bookRepository
        given(bookRepository.save(book)).willReturn(book);

        // Act
        Book savedBook = bookService.saveBook(book);

        // Assert
        assertThat(book).isEqualTo(savedBook);

        // Verify that the bookRepository.save() method was called
        verify(bookRepository).save(book);
    }

    @Test
    public void testDeleteBook() throws BookNotFoundException {
        // Arrange
        String title = "The Book Title";
        Book book =
                new Book()
                        .setBookId("1234567890")
                        .setTitle(title)
                        .setAuthor("Author")
                        .setText("Text");
        given(bookRepository.findBookByTitle(title)).willReturn(Optional.of(book));

        // Act
        bookService.deleteBook(title);

        // Assert
        verify(bookRepository, times(1)).findBookByTitle(title);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteAllCache() {
        // Arrange

        // Act
        String result = bookService.deleteAllCache();

        // Assert
        verifyNoInteractions(bookRepository);
    }

    @Test
    public void testDeleteAll() {
        // Arrange

        // Act
        bookService.deleteAll();

        // Assert
        verify(bookRepository).deleteAll();
    }
}
