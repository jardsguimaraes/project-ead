package com.ead.payment.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentStripeService;
import com.stripe.StripeClient;
import com.stripe.exception.CardException;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentStripeServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey}")
    private String secretKeyStripe;

    @Override
    public PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) {
        String paymentIntentId = null;
        String paymentIntentStatus = null;
        StripeClient client = new StripeClient(secretKeyStripe);

        try {
            var params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentModel.getValuePaid().multiply(new BigDecimal("100")).longValue())
                    .setCurrency("brl")
                    .setPaymentMethod(getPaymentMethod(creditCardModel.getCreditCardNumber().replaceAll(" ", "")))
                    .build();

            var paymentIntent = client.v1().paymentIntents().create(params);
            paymentIntentId = paymentIntent.getId();
            paymentIntentStatus = paymentIntent.getStatus();

            var intentConfirmParams = PaymentIntentConfirmParams.builder()
                    .setReturnUrl("https://example.com/return")
                    .build();
            var confirmPaymentIntent = client.v1().paymentIntents().confirm(paymentIntentId, intentConfirmParams);

            if ("succeeded".equals(confirmPaymentIntent.getStatus())) {
                paymentModel.setPaymentControl(PaymentControl.EFFECTED);
                paymentModel.setPaymentMessage("payment effected - paymentIntent: " + paymentIntentId);
                paymentModel.setPaymentCompletionDate(LocalDateTime.now(ZoneId.of("UTC")));

                log.info("payment effected with success: " + paymentIntentId);
            } else {
                paymentModel.setPaymentControl(PaymentControl.ERROR);
                paymentModel.setPaymentMessage("payment error v1 - paymentIntente: " + paymentIntentId);
            }
        } catch (CardException cardException) {
            log.error("A payment error occured: {}" + cardException.getMessage());

            try {
                paymentModel.setPaymentControl(PaymentControl.REFUSED);
                var paymentIntent = client.v1().paymentIntents().retrieve(paymentIntentId);
                var paymentMessageError = """
                        payment refused v1 - paymentIntent: %s, cause: %s, message: %s""".formatted(
                        paymentIntentId, paymentIntent.getLastPaymentError().getCode(),
                        paymentIntent.getLastPaymentError().getMessage());
                paymentModel.setPaymentMessage(paymentMessageError);
            } catch (Exception e) {
                log.error("Another problem occured, meybe unrelated to Stripe, with cause: {}", e.getMessage());
                paymentModel.setPaymentMessage("payment refused v2 - paymentIntent: " + paymentIntentId);
            }
        } catch (Exception e) {
            log.error("Another problem ocurrend, maybe unrelated to Stripe, with cause: {}", e.getMessage());
            paymentModel.setPaymentControl(PaymentControl.ERROR);
            paymentModel.setPaymentMessage("payment error v2 - paymentIntent: " + paymentIntentId);
        }

        return paymentModel;
    }

    private String getPaymentMethod(String creditCardNumber) {
        return switch (creditCardNumber) {
            case "4242424242424242" -> "pm_card_visa";
            case "5555555555554444" -> "pm_card_mastercard";
            case "4000000000009995" -> "pm_card_visa_chargeDeclinedInsufficientFunds";
            case "4000000000000127" -> "pm_card_chargeDeclinedIncorrectCvc";
            default -> "pm_card_visa_chargeDeclined";
        };
    }

}
