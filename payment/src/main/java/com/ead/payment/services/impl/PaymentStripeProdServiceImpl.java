package com.ead.payment.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentStripeService;
import com.stripe.StripeClient;
import com.stripe.exception.CardException;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;

import lombok.extern.log4j.Log4j2;

@Primary
@Log4j2
@Service
public class PaymentStripeProdServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey.prod}")
    private String secretKeyStripe;

    @Override
    public PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) {
        StripeClient client = new StripeClient(secretKeyStripe);
        String paymentIntentId = null;

        try {
            var params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentModel.getValuePaid().multiply(new BigDecimal("100")).longValue())
                    .setCurrency("brl")
                    .setPaymentMethod("card")
                    .build();

            var paymentIntent = client.v1().paymentIntents().create(params);
            paymentIntentId = paymentIntent.getId();

            var paramsPaymentMethod = PaymentMethodCreateParams.builder()
                    .setType(PaymentMethodCreateParams.Type.CARD)
                    .setCard(
                            PaymentMethodCreateParams.CardDetails.builder()
                                    .setNumber(creditCardModel.getCreditCardNumber().replaceAll(" ", ""))
                                    .setExpMonth(Long.parseLong(creditCardModel.getExpirationDate().split("/")[0]))
                                    .setExpYear(Long.parseLong(creditCardModel.getExpirationDate().split("/")[1]))
                                    .setCvc(creditCardModel.getCvvCode())
                                    .build())
                    .setBillingDetails(
                            PaymentMethodCreateParams.BillingDetails.builder()
                                    .setName(creditCardModel.getCardHolderFullName())
                                    .build())
                    .build();

            var paymentMethod = PaymentMethod.create(paramsPaymentMethod);

            var paramsPaymentConfirm = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(paymentMethod.getId())
                    .build();

            var confirmPaymentIntent = paymentIntent.confirm(paramsPaymentConfirm);

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

}
