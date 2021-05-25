package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.exception.BookNotFoundException;
import com.mongodb.redis.integration.request.BookDTO;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveCachingService {

    private final ReactiveBookService reactiveBookService;
    private final ReactiveRedisTemplate<String, BookDTO> reactiveJsonBookRedisTemplate;

    private ReactiveHashOperations<String, String, BookDTO> bookReactiveHashOperations;

    /** KeyName of the region where cache stored. */
    public static final String CACHEABLES_REGION_KEY = "reactivebooks";

    @PostConstruct
    void setUpReactiveHashOperations() {
        bookReactiveHashOperations = this.reactiveJsonBookRedisTemplate.opsForHash();
    }

    public Flux<BookDTO> findAllBooks() {
        return this.bookReactiveHashOperations
                .values(CACHEABLES_REGION_KEY)
                .log("Fetching from cache")
                .switchIfEmpty(
                        reactiveBookService
                                .findAllBooks()
                                .log("Fetching from DB")
                                .flatMap(this::convertToBookDTO)
                                .flatMap(this::putToCache));
    }

    private Mono<BookDTO> convertToBookDTO(Book book) {
        return Mono.just(
                new BookDTO(book.getBookId(), book.getTitle(), book.getAuthor(), book.getText()));
    }

    private Mono<BookDTO> putToCache(BookDTO bookDTO) {
        return this.bookReactiveHashOperations
                .put(CACHEABLES_REGION_KEY, bookDTO.getBookId(), bookDTO)
                .log("Pushing to Cache")
                .thenReturn(bookDTO);
    }

    public Mono<BookDTO> getBookById(String bookId) {
        return this.bookReactiveHashOperations
                .get(CACHEABLES_REGION_KEY, bookId)
                .log("Fetching from cache")
                .switchIfEmpty(
                        this.reactiveBookService
                                .getBookById(bookId)
                                .flatMap(this::convertToBookDTO)
                                .flatMap(this::putToCache));
    }

    public Mono<Long> deleteBook(String bookId) {
        return this.reactiveBookService
                .deleteBook(bookId)
                .flatMap(
                        book ->
                                this.bookReactiveHashOperations
                                        .remove(CACHEABLES_REGION_KEY, bookId)
                                        .log("Deleting From Cache"))
                .switchIfEmpty(
                        Mono.error(
                                () ->
                                        new BookNotFoundException(
                                                "Book with Id " + bookId + " Not Found")));
    }

    public Mono<Boolean> deleteAll() {
        return this.reactiveBookService
                .deleteAll()
                .flatMap(
                        aVoid ->
                                this.bookReactiveHashOperations
                                        .delete(CACHEABLES_REGION_KEY)
                                        .log("Deleting All Cache"));
    }

    public Mono<BookDTO> createBook(BookDTO bookDTO) {
        Book book = convertToBook(bookDTO);
        return this.reactiveBookService
                .createBook(book)
                .flatMap(this::convertToBookDTO)
                .flatMap(this::putToCache);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return new Book(
                bookDTO.getBookId(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getText());
    }

    public Mono<BookDTO> updateBook(String bookId, BookDTO validatedBook) {
        Book book = convertToBook(validatedBook);
        return this.reactiveBookService
                .updateBook(bookId, book)
                .flatMap(this::convertToBookDTO)
                .flatMap(this::putToCache);
    }
}
