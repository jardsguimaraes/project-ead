package com.ead.authuser.dtos.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PageMetadata(
        int size,
        long totalElements,
        int totalPages,
        int number) {
}
