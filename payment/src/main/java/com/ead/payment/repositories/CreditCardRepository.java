package com.ead.payment.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.UserModel;

public interface CreditCardRepository
                extends JpaRepository<CreditCardModel, UUID>, JpaSpecificationExecutor<CreditCardModel> {

        Optional<CreditCardModel> findByUser(UserModel userModel);

}
