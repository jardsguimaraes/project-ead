package com.ead.payment.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ead.payment.dtos.PaymentCommandRecordDto;

@Component
public class PaymentCommadPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${ead.broker.exchange.paymentCommadExchange}")
    private String paymentCommadExchange;

    @Value(value = "${ead.broker.key.paymentCommadKey}")
    private String paymentCommandKey;

    public PaymentCommadPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPaymentCommand(PaymentCommandRecordDto paymentCommandRecordDto) {
        rabbitTemplate.convertAndSend(paymentCommadExchange, paymentCommandKey, paymentCommandRecordDto);
    }
}
