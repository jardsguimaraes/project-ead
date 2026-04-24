package com.ead.course.dots;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRecordDto(
    @NotNull(message = "UserId is mandatory")
    UUID userId
) {
    
}
