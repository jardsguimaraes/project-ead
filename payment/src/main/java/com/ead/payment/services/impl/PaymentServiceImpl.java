package com.ead.payment.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.payment.dtos.PaymentCommandRecordDto;
import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.enums.PaymentStatus;
import com.ead.payment.exeptions.ExternalNotFoundException;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;
import com.ead.payment.publishers.PaymentCommadPublisher;
import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.repositories.PaymentRepository;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.PaymentService;
import com.ead.payment.services.PaymentStripeService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final PaymentCommadPublisher paymentCommadPublisher;
    private final PaymentStripeService paymentStripeService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository,
            UserRepository userRepository, PaymentCommadPublisher paymentCommadPublisher,
            PaymentStripeService paymentStripeService) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
        this.paymentCommadPublisher = paymentCommadPublisher;
        this.paymentStripeService = paymentStripeService;
    }

    @Override
    public PaymentModel requestPayment(PaymentRequestRecordDto paymentRequestRecordDto, UserModel userModel) {
        var creditCardModel = creditCardRepository
                .findByUser(userModel)
                .orElseGet(CreditCardModel::new);

        BeanUtils.copyProperties(paymentRequestRecordDto, creditCardModel);
        creditCardModel.setUser(userModel);
        creditCardRepository.save(creditCardModel);

        var paymentModel = new PaymentModel();

        paymentModel.setPaymentControl(PaymentControl.REQUESTED);
        paymentModel.setPaymentRequestDate(LocalDateTime.now(ZoneId.of("UTC")));
        paymentModel.setPaymentExpirationDate(LocalDateTime.now(ZoneId.of("UTC")).plusMonths(12));
        paymentModel.setLastDigitsCreditCard(paymentRequestRecordDto.creditCardNumber()
                .substring(paymentRequestRecordDto.creditCardNumber().length() - 4));
        paymentModel.setValuePaid(paymentRequestRecordDto.valuePaid());
        paymentModel.setUser(userModel);

        paymentRepository.save(paymentModel);

        try {
            var paymentCommandRecordDto = new PaymentCommandRecordDto(userModel.getUserId(),
                    paymentModel.getPaymentId(),
                    creditCardModel.getCardId());
            paymentCommadPublisher.publishPaymentCommand(paymentCommandRecordDto);
        } catch (Exception e) {
            logger.error("Error: sending payment command message with cause {}", e.getMessage());
        }

        return paymentModel;
    }

    @Override
    public Optional<PaymentModel> findLastPaymentByUser(UserModel userModel) {
        return paymentRepository.findTopByUserOrderByPaymentRequestDateDesc(userModel);
    }

    @Override
    public Page<PaymentModel> findAllByUser(Specification<PaymentModel> spec, Pageable pageable) {
        return paymentRepository.findAll(spec, pageable);
    }

    @Override
    public PaymentModel findPaymentByUser(UUID userId, UUID paymentId) {
        return paymentRepository.findPaymentByUser(userId, paymentId)
                .orElseThrow(() -> new ExternalNotFoundException("ERROR: Payment not found for this user"));
    }

    @Override
    public void makePayment(PaymentCommandRecordDto paymentCommandRecordDto) {
        var paymentModel = paymentRepository.findById(paymentCommandRecordDto.paymentId())
                .orElseThrow(() -> new ExternalNotFoundException("Error: Paymeny not found!"));
        var userModel = userRepository.findById(paymentCommandRecordDto.userId())
                .orElseThrow(() -> new ExternalNotFoundException("Error: User not found for Payment"));
        var creditCardModel = creditCardRepository.findById(paymentCommandRecordDto.cardId())
                .orElseThrow(() -> new ExternalNotFoundException("Error: creditCard not found"));

        paymentModel = paymentStripeService.processStripePayment(paymentModel, creditCardModel);
        paymentRepository.save(paymentModel);

        if (PaymentControl.EFFECTED.equals(paymentModel.getPaymentControl())) {
            userModel.setPaymentStatus(PaymentStatus.PAYING);
            userModel.setLastPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
            userModel.setPaymentExpirationDate(LocalDateTime.now(ZoneId.of("UTC")).plusMonths(12));

            if (userModel.getFirstPaymentDate() == null) {
                userModel.setFirstPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
            }
        } else {
            userModel.setPaymentStatus(PaymentStatus.DEBTOR);
        }

        userRepository.save(userModel);

        // send payment event
    }
}
