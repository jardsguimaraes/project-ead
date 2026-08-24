package com.ead.payment.services.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentStripeService;
import com.stripe.StripeClient;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentStripeServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey}")
    private String secretKeyStripe;

    @Override
    public PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) {
        try {
            String paymentIntentId = null;
            String paymentIntentStatus = null;
            StripeClient client = new StripeClient(secretKeyStripe);

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
