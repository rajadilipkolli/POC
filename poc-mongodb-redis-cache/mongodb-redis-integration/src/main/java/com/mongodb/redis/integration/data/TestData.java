/* Licensed under Apache-2.0 2021-2025 */
package com.mongodb.redis.integration.data;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Configuration
@Profile({"local", "test"})
public class TestData {

    private static final Logger log = LoggerFactory.getLogger(TestData.class);

    private final BookRepository bookRepository;

    public TestData(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    void loadData() {
        Book book = new Book().setTitle("SB2").setBookId("1").setAuthor("raja");
        log.info("saving dummy data");
        this.bookRepository.save(book);
    }
}
