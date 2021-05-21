package com.mongodb.redis.integration.document;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @Id private String bookId;

    @Indexed(unique = true)
    @NotBlank(message = "Book title can't be Blank")
    @Size(max = 140, message = "Book title size must be between 0 and 140")
    private String title;

    private String author;

    private String text;

    @Version private Long version;
}
