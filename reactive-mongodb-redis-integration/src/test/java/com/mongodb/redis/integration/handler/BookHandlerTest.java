package com.mongodb.redis.integration.handler;

@ExtendWith(MockitoExtension.class)
public class BookHandlerTest {

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
        when(reactiveCachingService.getBookById(anyString())).thenReturn(Mono.just(new BookDTO("1", "book1", "author1")));
        StepVerifier.create(bookHandler.getBook(MockServerRequest.builder().build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    public void testGetBookNotFound() {
        when(reactiveCachingService.getBookById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.getBook(MockServerRequest.builder().build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NOT_FOUND))
                .verifyComplete();
    }

    @Test
    public void testDeleteBook() {
        when(reactiveCachingService.deleteBook(anyString())).thenReturn(Mono.just(new BookDTO("1", "book1", "author1")));
        StepVerifier.create(bookHandler.deleteBook(MockServerRequest.builder().build()))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.ACCEPTED))
                .verifyComplete();
    }

    @Test
    public void testDeleteBookNotFound() {
        when(reactiveCachingService.deleteBook(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(bookHandler.deleteBook(MockServerRequest.builder().build()))
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

