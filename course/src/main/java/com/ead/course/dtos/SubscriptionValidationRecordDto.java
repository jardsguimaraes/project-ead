package com.ead.course.dtos;

import java.util.UUID;

public record SubscriptionValidationRecordDto(
        UUID courseId,
        UUID userId,
        String userStatus) {

}
