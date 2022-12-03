/* Licensed under Apache-2.0 2021-2022 */
package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TagDTO(@JsonProperty("name") String name) {}
