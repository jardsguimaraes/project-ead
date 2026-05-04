package com.ead.authuser.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ead.authuser.dtos.UserEventDto;

@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${ead.broker.exchange.userEvent}")
    private String exchangeUserevent;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserEvent(UserEventDto userEventDto) {
        var routingKey = "";
        rabbitTemplate.convertAndSend(exchangeUserevent, routingKey, userEventDto);
    }
}
