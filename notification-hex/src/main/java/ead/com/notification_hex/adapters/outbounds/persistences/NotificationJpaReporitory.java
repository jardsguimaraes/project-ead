package ead.com.notification_hex.adapters.outbounds.persistences;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ead.com.notification_hex.adapters.outbounds.entities.NotificationEntity;

public interface NotificationJpaReporitory extends JpaRepository<NotificationEntity, UUID> {

}
