/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.data;

import com.mongodb.redis.integration.document.Book;
import com.mongodb.redis.integration.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Configuration
@Profile({"local", "test"})
@Slf4j
public class TestData {

    @Autowired BookRepository bookRepository;

    @EventListener(ApplicationReadyEvent.class)
    void loadData() {
        Book book = Book.builder().title("SB2").bookId("1").author("raja").build();
        log.info("saving dummy data");
        this.bookRepository.save(book);
    }
}
