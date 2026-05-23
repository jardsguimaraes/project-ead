package ead.com.notification.dtos;

import ead.com.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public record NotificationRecordDto(
        @NotNull NotificationStatus notificationStatus) {

}
