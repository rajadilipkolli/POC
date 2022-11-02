package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TagDTO(@JsonProperty("name") String name) {}
