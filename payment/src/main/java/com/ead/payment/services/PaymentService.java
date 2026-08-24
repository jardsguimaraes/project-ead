package com.ead.payment.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.payment.dtos.PaymentCommandRecordDto;
import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;

public interface PaymentService {

    PaymentModel requestPayment(PaymentRequestRecordDto paymentRequestRecordDto, UserModel userModel);

    Optional<PaymentModel> findLastPaymentByUser(UserModel userModel);

    Page<PaymentModel> findAllByUser(Specification<PaymentModel> spec, Pageable pageable);

    PaymentModel findPaymentByUser(UUID userId, UUID paymentId);

    void makePayment(PaymentCommandRecordDto paymentCommandRecordDto);

}
