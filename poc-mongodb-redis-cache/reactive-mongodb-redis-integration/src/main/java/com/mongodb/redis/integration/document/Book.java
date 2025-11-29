/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.document;

import java.io.Serial;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Book implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @Id private String bookId;

    @Indexed(unique = true)
    private String title;

    private String author;

    public Book(String bookId, String title, String author, String text, Long version) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.text = text;
        this.version = version;
    }

    public Book() {}

    private String text;

    @Version private Long version;

    public String getBookId() {
        return bookId;
    }

    public Book setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getText() {
        return text;
    }

    public Book setText(String text) {
        this.text = text;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
