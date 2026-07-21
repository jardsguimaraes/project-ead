package ead.com.notification_hex.adapters.outbounds.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import ead.com.notification_hex.core.domain.enums.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "TB_NOTIFICATIONS")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

}
