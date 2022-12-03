/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.event.CreatedBookEvent;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveBookService {

    private final ReactiveBookRepository reactiveBookRepository;

    private final ApplicationEventPublisher publisher;

    public Mono<Book> findBookByTitle(String title) {
        return this.reactiveBookRepository
                .findByTitle(title)
                .switchIfEmpty(
                        Mono.error(
                                new BookNotFoundException(
                                        "Book with Title " + title + " NotFound!")));
    }

    public Flux<Book> findAllBooks() {
        return this.reactiveBookRepository.findAll();
    }

    public Mono<Book> getBookById(String bookId) {
        return this.reactiveBookRepository.findById(bookId).log("Fetching from Database");
    }

    public Mono<Book> createBook(Book bookToPersist) {
        Consumer<Book> publishEventConsumer =
                persistedBook -> this.publisher.publishEvent(new CreatedBookEvent(persistedBook));
        return this.reactiveBookRepository
                .save(bookToPersist)
                .log("Saving to DB")
                .doOnSuccess(publishEventConsumer)
                .doOnError(
                        error ->
                                log.error(
                                        "The following error happened on creatingBook method!",
                                        error));
    }

    public Mono<Book> updateBook(String bookId, Book requestedBook) {
        return this.reactiveBookRepository
                .findById(bookId)
                .switchIfEmpty(Mono.empty())
                .map(updateBookFunction(requestedBook))
                .flatMap(this.reactiveBookRepository::save)
                .log("Updating in Database");
    }

    public Mono<Book> deleteBook(String bookId) {
        return this.reactiveBookRepository
                .findById(bookId)
                .log("finding in Database")
                .flatMap(
                        book ->
                                this.reactiveBookRepository
                                        .deleteById(book.getBookId())
                                        .log("Deleting from Database")
                                        .thenReturn(book))
                .switchIfEmpty(Mono.empty());
    }

    public Mono<Void> deleteAll() {
        return this.reactiveBookRepository.deleteAll().log("Deleting from Database");
    }

    private Function<Book, Book> updateBookFunction(Book requestedBook) {
        return persistedBook -> {
            persistedBook.setAuthor(requestedBook.getAuthor());
            persistedBook.setText(requestedBook.getText());
            persistedBook.setTitle(requestedBook.getTitle());
            return persistedBook;
        };
    }
}
