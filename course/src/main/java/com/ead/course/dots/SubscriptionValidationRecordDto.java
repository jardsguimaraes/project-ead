package com.ead.course.dots;

import java.util.UUID;

public record SubscriptionValidationRecordDto(
        UUID courseId,
        UUID userId,
        String userStatus) {

}
