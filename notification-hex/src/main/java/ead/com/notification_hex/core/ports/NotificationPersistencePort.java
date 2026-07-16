package ead.com.notification_hex.core.ports;

import java.util.List;
import java.util.UUID;

import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;

public interface NotificationPersistencePort {

    NotificationDomain saveNotification(NotificationDomain notificationDomain);

    List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus,
            PageInfo pageInfo);

    NotificationDomain findByNotificationIdAndUserId(UUID notificationId, UUID userId);

    NotificationDomain update(NotificationStatus notificationStatus, NotificationDomain notificationDomain);
}
