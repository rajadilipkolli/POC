/**
 * Copyright (c) Raja Dilip Chowdary Kolli. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.poc.mongodbredisintegration.document;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

/**
 * <p>Book class.</p>
 *
 * @author rajakolli
 * @version 0 : 5
 * @since July 2017
 */
@Data
@Document
@Builder
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    @Indexed
    @NotBlank
    @Size(max = 140)
    private String title;
    private String author;
    private String text;
    @Version
    private int version;
}
