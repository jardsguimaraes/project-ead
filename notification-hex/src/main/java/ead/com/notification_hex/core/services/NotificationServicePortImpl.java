package ead.com.notification_hex.core.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import ead.com.notification_hex.core.ports.NotificationServicePort;

public class NotificationServicePortImpl implements NotificationServicePort {

    private final NotificationServicePort notificationServicePort;

    public NotificationServicePortImpl(NotificationServicePort notificationServicePort) {
        this.notificationServicePort = notificationServicePort;
    }

    @Override
    public NotificationDomain saveNotification(NotificationDomain notificationDomain) {
        notificationDomain.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationDomain.setNotificationStatus(NotificationStatus.CREATED);
        return notificationServicePort.saveNotification(notificationDomain);
    }

    @Override
    public List<NotificationDomain> findAllNotificationByUser(UUID userId, PageInfo pageInfo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllNotificationByUser'");
    }

    @Override
    public NotificationDomain findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNotificationIdAndUserId'");
    }

    @Override
    public NotificationDomain updateNotification(NotificationStatus notificationStatus,
            NotificationDomain notificationDomain) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNotification'");
    }

}
