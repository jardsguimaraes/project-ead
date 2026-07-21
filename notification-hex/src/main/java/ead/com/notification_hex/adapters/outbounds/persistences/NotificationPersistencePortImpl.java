package ead.com.notification_hex.adapters.outbounds.persistences;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import ead.com.notification_hex.adapters.outbounds.entities.NotificationEntity;
import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.domain.PageInfo;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import ead.com.notification_hex.core.ports.NotificationPersistencePort;

@Component
public class NotificationPersistencePortImpl implements NotificationPersistencePort {

    private final NotificationJpaReporsitory notificationJpaReporitory;
    private final ModelMapper modelMapper;

    public NotificationPersistencePortImpl(NotificationJpaReporsitory notificationJpaReporitory,
            ModelMapper modelMapper) {
        this.notificationJpaReporitory = notificationJpaReporitory;
        this.modelMapper = modelMapper;
    }

    @Override
    public NotificationDomain saveNotification(NotificationDomain notificationDomain) {
        var notificationEntity = notificationJpaReporitory
                .save(modelMapper.map(notificationDomain, NotificationEntity.class));
        return modelMapper.map(notificationEntity, NotificationDomain.class);
    }

    @Override
    public List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId,
            NotificationStatus notificationStatus, PageInfo pageInfo) {
        var pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        return notificationJpaReporitory.findAllByUserIdAndNotificationStatus(userId, notificationStatus, pageable)
                .stream()
                .map(entity -> modelMapper.map(entity, NotificationDomain.class))
                .collect(Collectors.toList());
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
