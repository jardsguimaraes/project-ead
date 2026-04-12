package com.ead.course.dots;

import jakarta.validation.constraints.NotBlank;

public record ModuleRecordDto(
    @NotBlank
    String title,
    
    @NotBlank
    String description
) {
    
}
