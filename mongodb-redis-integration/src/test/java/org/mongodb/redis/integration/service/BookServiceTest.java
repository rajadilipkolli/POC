package org.mongodb.redis.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.exception.BookNotFoundException;
import org.mongodb.redis.integration.repository.BookRepository;

@TestInstance(Lifecycle.PER_CLASS)
class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	private BookService bookService;

	@BeforeAll
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		bookService = new BookServiceImpl(bookRepository);
	}

	@Test
	public void getCarDetails_returnCarInfo() {
		given(bookRepository.findBookByTitle("JUNIT_TITLE"))
				.willReturn(Book.builder().title("JUNIT_TITLE").author("JUNIT_AUTHOR")
						.bookId("JUNIT").text("JUNIT_TEXT").version(1L).build());

		Book book = bookService.findBookByTitle("JUNIT_TITLE");

		assertThat(book.getTitle()).isEqualTo("JUNIT_TITLE");
		assertThat(book.getAuthor()).isEqualTo("JUNIT_AUTHOR");
		assertThat(book.getText()).isEqualTo("JUNIT_TEXT");
	}

	@Test
	public void getCarDetails_whenCarNotFound() throws Exception {
		given(bookRepository.findBookByTitle("JUNIT_TITLE")).willReturn(null);

		bookService.findBookByTitle("prius");
		assertThatExceptionOfType(BookNotFoundException.class).isThrownBy(() -> {
			throw new BookNotFoundException("boom!");
		}).withMessage("%s!", "boom").withMessageContaining("boom").withNoCause();
	}

}
