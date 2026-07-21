package ead.com.notification_hex.core.ports;

import java.util.List;
import java.util.UUID;

import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;

public interface NotificationServicePort {

    NotificationDomain saveNotification(NotificationDomain notificationDomain);

    List<NotificationDomain> findAllNotificationByUser(UUID userId, PageInfo pageInfo);

    NotificationDomain findByNotificationIdAndUserId(UUID notificationId, UUID userId);

    NotificationDomain updateNotification(NotificationStatus notificationStatus, NotificationDomain notificationDomain);
}
