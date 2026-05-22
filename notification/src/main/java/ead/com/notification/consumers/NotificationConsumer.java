package ead.com.notification.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import ead.com.notification.dtos.NotificationRecordCommandDto;
import ead.com.notification.services.NotificationService;

@Component
public class NotificationConsumer {
    
    private NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}", durable = "true"),
        exchange = @Exchange(value = "${ead.broker.exchange.notificationCommandExchange}",
            type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
        key = "${ead.broker.key.notificationCommandKey}")
    )
    public void listen(@Payload NotificationRecordCommandDto notificationRecordCommandDto) {
        notificationService.saveNotification(notificationRecordCommandDto);
    }
}
