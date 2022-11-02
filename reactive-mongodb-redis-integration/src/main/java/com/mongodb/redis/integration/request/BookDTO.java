package com.mongodb.redis.integration.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class BookDTO implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @Id private String bookId;

    @NotBlank(message = "Book title can't be Blank")
    @Size(max = 140, message = "Book title size must be between 0 and 140")
    private String title;

    private String author;

    private String text;

    @JsonIgnore private Long version;
}
