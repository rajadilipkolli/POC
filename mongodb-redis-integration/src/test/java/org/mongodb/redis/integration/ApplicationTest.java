package org.mongodb.redis.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mongodb.redis.integration.document.Book;
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

	@Test
	public void getBookByTitle_returnsBookDetails() throws Exception {
		// arrange
		Book book = Book.builder().title("MongoDbCookBook").author("Raja").build();
		ResponseEntity<Book> response = restTemplate.postForEntity("/book/saveBook", book,
				Book.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");

		// act
		response = restTemplate
				.getForEntity("/book/findByTitle/MongoDbCookBook", Book.class);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getTitle()).isEqualTo("MongoDbCookBook");
		assertThat(response.getBody().getAuthor()).isEqualTo("Raja");

	}

}
