package com.ead.payment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ead.payment.models.CreditCardModel;

public interface CreditCardRepository
        extends JpaRepository<CreditCardModel, UUID>, JpaSpecificationExecutor<CreditCardModel> {

}
