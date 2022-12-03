/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.event;

import com.mongodb.redis.integration.document.Book;
import java.io.Serial;
import org.springframework.context.ApplicationEvent;

public class CreatedBookEvent extends ApplicationEvent {

    @Serial private static final long serialVersionUID = 1L;

    public CreatedBookEvent(Book book) {
        super(book);
    }
}
