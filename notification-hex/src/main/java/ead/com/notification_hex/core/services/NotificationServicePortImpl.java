package ead.com.notification_hex.core.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import ead.com.notification_hex.core.ports.NotificationPersistencePort;
import ead.com.notification_hex.core.ports.NotificationServicePort;

public class NotificationServicePortImpl implements NotificationServicePort {

    private final NotificationPersistencePort notificationPersistencePort;

    public NotificationServicePortImpl(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    @Override
    public NotificationDomain saveNotification(NotificationDomain notificationDomain) {
        notificationDomain.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationDomain.setNotificationStatus(NotificationStatus.CREATED);
        return notificationPersistencePort.saveNotification(notificationDomain);
    }

    @Override
    public List<NotificationDomain> findAllNotificationByUser(UUID userId, PageInfo pageInfo) {
        return notificationPersistencePort.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageInfo);
    }

    @Override
    public NotificationDomain findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationPersistencePort.findByNotificationIdAndUserId(notificationId, userId);
    }

    @Override
    public NotificationDomain updateNotification(NotificationStatus notificationStatus,
            NotificationDomain notificationDomain) {
        return notificationPersistencePort.update(notificationStatus, notificationDomain);
    }

}
