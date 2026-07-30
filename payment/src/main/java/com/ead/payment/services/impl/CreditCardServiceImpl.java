package com.ead.payment.services.impl;

import org.springframework.stereotype.Service;

import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.services.CreditCardService;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }
}
