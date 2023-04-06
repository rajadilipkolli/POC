/* Licensed under Apache-2.0 2021-2023 */
package com.example.poc.reactive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String title;

    private String content;
}
