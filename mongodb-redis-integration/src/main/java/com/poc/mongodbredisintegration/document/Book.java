/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.poc.mongodbredisintegration.document;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>Book class.</p>
 *
 * @author Raja Kolli
 * @since 0.0.5
 */
@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String bookId;

	@Indexed
	@NotBlank
	@Size(max = 140)
	private String title;

	private String author;

	private String text;

	@Version
	private Long version;

}
