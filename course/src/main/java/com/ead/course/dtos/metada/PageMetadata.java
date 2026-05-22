package com.ead.course.dtos.metada;

public record PageMetadata(
        int size,
        long totalElements,
        int totalPages,
        int number) {

}
