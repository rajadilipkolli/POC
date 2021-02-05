package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.event.BookCreatedEvent;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.ReactiveBookRepository;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
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
            Mono.error(new BookNotFoundException("Book with Title " + title + " NotFound!")));
  }

  public Publisher<Book> findAllBooks() {
    return this.reactiveBookRepository.findAll().log("Fetching from Database");
  }

  public Mono<Book> getBookById(String bookId) {
    return this.reactiveBookRepository.findById(bookId);
  }

  public Mono<Book> createBook(Book bookToPersist) {
    Consumer<Book> publishEventConsumer =
        persistedBook -> this.publisher.publishEvent(new BookCreatedEvent(persistedBook));
    return this.reactiveBookRepository
        .save(bookToPersist)
        .log("Saving to DB")
        .doOnSuccess(publishEventConsumer)
        .doOnError(
            error -> log.error("The following error happened on creatingBook method!", error));
  }

  public Mono<Book> updateBook(String bookId, Book requestedBook) {
    return this.reactiveBookRepository
        .findById(bookId)
        .log()
        .map(updateBookFunction(requestedBook))
        .flatMap(this.reactiveBookRepository::save)
        .log("Updating in Database");
  }

  private Function<Book, Book> updateBookFunction(Book requestedBook) {
    return persistedBook -> {
      persistedBook.setAuthor(requestedBook.getAuthor());
      persistedBook.setText(requestedBook.getText());
      persistedBook.setTitle(requestedBook.getTitle());
      return persistedBook;
    };
  }

  public Mono<Void> deleteBook(String bookId) {
    return getBookById(bookId)
        .flatMap(book -> this.reactiveBookRepository.deleteById(book.getBookId()));
  }
}
