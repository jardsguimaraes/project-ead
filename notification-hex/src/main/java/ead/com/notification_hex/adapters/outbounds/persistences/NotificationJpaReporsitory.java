package ead.com.notification_hex.adapters.outbounds.persistences;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ead.com.notification_hex.adapters.outbounds.entities.NotificationEntity;
import ead.com.notification_hex.core.domain.enums.NotificationStatus;

public interface NotificationJpaReporsitory extends JpaRepository<NotificationEntity, UUID> {

    Page<NotificationEntity> findAllByUserIdAndNotificationStatus(UUID userId,
            NotificationStatus notificationStatus, PageRequest pageable);

}
