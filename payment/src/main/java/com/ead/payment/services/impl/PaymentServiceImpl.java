package com.ead.payment.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.exeptions.ExternalNotFoundException;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;
import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.repositories.PaymentRepository;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
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

        // send request to queue

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
}
