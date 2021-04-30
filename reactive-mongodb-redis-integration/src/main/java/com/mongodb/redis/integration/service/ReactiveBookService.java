package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.event.BookCreatedEvent;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.repository.BookReactiveRepository;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveBookService {

  /** KeyName of the region where cache stored. */
  public static final String CACHEABLES_REGION_KEY = "reactivebooks";

  private final BookReactiveRepository bookReactiveRepository;

  private final ApplicationEventPublisher publisher;

  private final ReactiveRedisTemplate<String, Book> reactiveJsonBookRedisTemplate;

  private ReactiveHashOperations<String, String, Book> bookReactiveHashOperations;

  @PostConstruct
  void setUpReactiveHashOperations() {
    bookReactiveHashOperations = this.reactiveJsonBookRedisTemplate.opsForHash();
  }

  public Mono<Book> findBookByTitle(String title) {
    return this.bookReactiveRepository
        .findByTitle(title)
        .switchIfEmpty(
            Mono.error(new BookNotFoundException("Book with Title " + title + " NotFound!")));
  }

  public Flux<Book> findAllBooks() {

    return this.bookReactiveHashOperations
        .values(CACHEABLES_REGION_KEY)
        .log("Fetching from cache")
        .switchIfEmpty(
            this.bookReactiveRepository
                .findAll()
                .log("Fetching from Database")
                .flatMap(this::putToCache));
  }

  public Mono<Book> getBookById(String bookId) {
    return this.bookReactiveHashOperations
        .get(CACHEABLES_REGION_KEY, bookId)
        .log("Fetching from cache")
        .switchIfEmpty(
            this.bookReactiveRepository
                .findById(bookId)
                .log("Fetching from Database")
                .flatMap(this::putToCache));
  }

  public Mono<Book> createBook(Book bookToPersist) {
    Consumer<Book> publishEventConsumer =
        persistedBook -> this.publisher.publishEvent(new BookCreatedEvent(persistedBook));
    return this.bookReactiveRepository
        .save(bookToPersist)
        .log("Saving to DB")
        .flatMap(this::putToCache)
        .doOnSuccess(publishEventConsumer)
        .doOnError(
            error -> log.error("The following error happened on creatingBook method!", error));
  }

  public Mono<Book> updateBook(String bookId, Book requestedBook) {
    return this.bookReactiveRepository
        .findById(bookId)
        .log()
        .map(updateBookFunction(requestedBook))
        .flatMap(this.bookReactiveRepository::save)
        .log("Updating in Database")
        .flatMap(this::putToCache);
  }

  public Mono<Book> deleteBook(String bookId) {
    return this.bookReactiveRepository
        .findById(bookId)
        .flatMap(
            book ->
                this.bookReactiveRepository
                    .deleteById(book.getBookId())
                    .log("Deleting from Database")
                    .thenReturn(book)
                    .flatMap(
                        returnedBook ->
                            this.bookReactiveHashOperations
                                .remove(CACHEABLES_REGION_KEY, bookId)
                                .log("Deleting From Cache")
                                .thenReturn(returnedBook)));
  }

  public Mono<Boolean> deleteAll() {
    return this.bookReactiveHashOperations.delete(CACHEABLES_REGION_KEY).log("Deleting All Cache");
  }

  private Mono<Book> putToCache(Book book) {
    return this.bookReactiveHashOperations
        .put(CACHEABLES_REGION_KEY, book.getBookId(), book)
        .log("Pushing to Cache")
        .thenReturn(book);
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
