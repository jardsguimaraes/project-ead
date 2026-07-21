package ead.com.notification_hex.adapters.inbounds;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import ead.com.notification_hex.adapters.dots.NotificationRecordCommandDto;
import ead.com.notification_hex.core.domain.NotificationDomain;
import ead.com.notification_hex.core.ports.NotificationServicePort;

@Component
public class NotificationConsumer {

    private final NotificationServicePort notificationServicePort;

    public NotificationConsumer(NotificationServicePort notificationServicePort) {
        this.notificationServicePort = notificationServicePort;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}", durable = "true"), exchange = @Exchange(value = "${ead.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"), key = "${ead.broker.key.notificationCommandKey}"))
    public void listen(@Payload NotificationRecordCommandDto notificationRecordCommandDto) {
        var notificationDomain = new NotificationDomain();
        BeanUtils.copyProperties(notificationRecordCommandDto, notificationDomain);
        notificationServicePort.saveNotification(notificationDomain);
    }
}
