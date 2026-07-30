package com.ead.payment.services.impl;

import org.springframework.stereotype.Service;

import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.repositories.PaymentRepository;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
    }
}
