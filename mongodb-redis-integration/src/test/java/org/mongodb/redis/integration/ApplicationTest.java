package org.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mongodb.redis.integration.document.Book;
import org.mongodb.redis.integration.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Integration test for simple App.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void getBook_returnsBookDetails() throws Exception {
		// arrange
		Book book = Book.builder().title("MongoDbCookBook").author("Raja").build();
		this.bookRepository.save(book);

		// act
		ResponseEntity<Book> response = restTemplate
				.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja");

	}

}
