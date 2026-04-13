package com.ead.course.dots;

import jakarta.validation.constraints.NotBlank;

public record LessonRecordDto(
    @NotBlank
    String title,

    @NotBlank
    String description,

    @NotBlank
    String videoUrl) {
    
}
