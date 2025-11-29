/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public record BookDTO(
        @Id String bookId,
        @NotBlank(message = "Book title can't be Blank")
                @Size(max = 140, message = "Book title size must be between 0 and 140")
                String title,
        String author,
        String text,
        @JsonIgnore Long version)
        implements Serializable {}
