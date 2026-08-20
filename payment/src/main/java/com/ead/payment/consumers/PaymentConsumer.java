package com.ead.payment.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ead.payment.dtos.PaymentCommandRecordDto;
import com.ead.payment.services.PaymentService;

@Component
public class PaymentConsumer {

    private final PaymentService paymentService;

    public PaymentConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${ead.broker.queue.paymentCommandQueue.name}", durable = "true"), exchange = @Exchange(value = "${ead.broker.exchange.paymentCommadExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"), key = "${ead.broker.key.paymentCommadKey}"))
    public void listenPaymentCommand(@Payload PaymentCommandRecordDto paymentCommandRecordDto) {
        System.out.println(paymentCommandRecordDto.paymentId());
        System.out.println(paymentCommandRecordDto.userId());

        // make payment
    }
}
