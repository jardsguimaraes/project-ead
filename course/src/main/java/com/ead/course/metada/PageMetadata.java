package com.ead.course.metada;

public record PageMetadata(
        int size,
        long totalElements,
        int totalPages,
        int number) {

}
