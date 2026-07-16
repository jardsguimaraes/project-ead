package ead.com.notification_hex.adapters.outbounds.persistences;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ead.com.notification_hex.adapters.outbounds.entities.NotificationEntity;
import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import ead.com.notification_hex.core.ports.NotificationPersistencePort;

@Component
public class NotificationPersistencePortImpl implements NotificationPersistencePort {

    private final NotificationJpaReporitory notificationJpaReporitory;
    private final ModelMapper modelMapper;

    public NotificationPersistencePortImpl(NotificationJpaReporitory notificationJpaReporitory,
            ModelMapper modelMapper) {
        this.notificationJpaReporitory = notificationJpaReporitory;
        this.modelMapper = modelMapper;
    }

    @Override
    public NotificationDomain saveNotification(NotificationDomain notificationDomain) {
        var notificationEntity = notificationJpaReporitory.save(modelMapper.map(notificationDomain, NotificationEntity.class));
        return modelMapper.map(notificationEntity, NotificationDomain.class);
    }

    @Override
    public List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId,
            NotificationStatus notificationStatus, PageInfo pageInfo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByUserIdAndNotificationStatus'");
    }

    @Override
    public NotificationDomain findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNotificationIdAndUserId'");
    }

    @Override
    public NotificationDomain update(NotificationStatus notificationStatus, NotificationDomain notificationDomain) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}
