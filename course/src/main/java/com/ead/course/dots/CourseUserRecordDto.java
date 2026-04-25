package com.ead.course.dots;

import java.util.UUID;

public record CourseUserRecordDto(
    UUID courseId,
    UUID userId
) {
    
}
