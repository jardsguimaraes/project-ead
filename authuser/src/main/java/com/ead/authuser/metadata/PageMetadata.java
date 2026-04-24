package com.ead.authuser.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PageMetadata(
        int size,
        long totalElements,
        int totalPages,
        int number) {
}
