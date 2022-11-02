package com.example.poc.webmvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostCommentsDTO(@JsonProperty("review") String review) {}
