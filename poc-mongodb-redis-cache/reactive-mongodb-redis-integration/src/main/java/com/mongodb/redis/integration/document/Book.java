/* Licensed under Apache-2.0 2021-2022 */
package com.mongodb.redis.integration.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serial;
import java.io.Serializable;

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
    private String title;

    private String author;

    private String text;

    @Version private Long version;
}
