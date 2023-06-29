/* Licensed under Apache-2.0 2021-2023 */
package com.mongodb.redis.integration.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // @Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id private String id;

    private String description;

    private Double price;
}
