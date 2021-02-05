package com.mongodb.redis.integration.document;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;

@Setter
@Getter
@Builder
public class Book implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id private String bookId;

  @Indexed
  @NotBlank
  @Size(max = 140)
  private String title;

  private String author;

  private String text;

  @Version private Long version;
}
