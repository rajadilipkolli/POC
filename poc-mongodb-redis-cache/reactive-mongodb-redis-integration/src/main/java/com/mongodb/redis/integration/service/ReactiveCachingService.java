/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.service;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.request.BookDTO;
import jakarta.annotation.PostConstruct;
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
                .switchIfEmpty(getAllBooksFromDbAndPutInCache());
    }

    private Flux<BookDTO> getAllBooksFromDbAndPutInCache() {
        return reactiveBookService
                .findAllBooks()
                .log("Fetching from DB")
                .flatMap(this::convertToBookDTOMono)
                .flatMap(this::putToCache);
    }

    public Mono<BookDTO> getBookById(String bookId) {
        return this.bookReactiveHashOperations
                .get(CACHEABLES_REGION_KEY, bookId)
                .log("Fetching from cache")
                .switchIfEmpty(
                        getBookDTOAfterUpdatingCache(this.reactiveBookService.getBookById(bookId)));
    }

    public Mono<BookDTO> deleteBook(String bookId) {
        return this.reactiveBookService
                .deleteBook(bookId)
                .flatMap(
                        book ->
                                this.bookReactiveHashOperations
                                        .remove(CACHEABLES_REGION_KEY, bookId)
                                        .log("Deleting From Cache")
                                        .thenReturn(convertToBookDTO(book)))
                .switchIfEmpty(Mono.empty());
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
        return getBookDTOAfterUpdatingCache(this.reactiveBookService.createBook(book));
    }

    public Mono<BookDTO> updateBook(String bookId, BookDTO validatedBook) {
        Book book = convertToBook(validatedBook);
        return getBookDTOAfterUpdatingCache(this.reactiveBookService.updateBook(bookId, book));
    }

    // converts to BookDTO and updates Cache
    private Mono<BookDTO> getBookDTOAfterUpdatingCache(Mono<Book> bookMono) {
        return bookMono.flatMap(this::convertToBookDTOMono)
                .flatMap(this::putToCache)
                .switchIfEmpty(Mono.empty());
    }

    // convert to Book from BookDTO
    private Book convertToBook(BookDTO bookDTO) {
        return new Book(
                bookDTO.getBookId(),
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getText(),
                bookDTO.getVersion());
    }

    // convert to Mono<BookDTO> from Book
    private Mono<BookDTO> convertToBookDTOMono(Book book) {
        return Mono.just(convertToBookDTO(book));
    }

    private BookDTO convertToBookDTO(Book book) {
        return new BookDTO(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getText(),
                book.getVersion());
    }

    // updates the value in cache
    private Mono<BookDTO> putToCache(BookDTO bookDTO) {
        return this.bookReactiveHashOperations
                .put(CACHEABLES_REGION_KEY, bookDTO.getBookId(), bookDTO)
                .log("Pushing to Cache")
                .thenReturn(bookDTO);
    }
}
