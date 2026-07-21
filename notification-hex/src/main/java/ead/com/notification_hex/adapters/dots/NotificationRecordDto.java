package ead.com.notification_hex.adapters.dots;

import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public record NotificationRecordDto(
                @NotNull NotificationStatus notificationStatus) {

}
