package com.ead.payment.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentRequestRecordDto(
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 5, fraction = 2) BigDecimal valuePaid,

        @NotBlank String cardHolderFullName,

        @NotBlank @CPF String cardHolderCpf,

        @NotBlank @Size(min = 16, max = 20) String creditCardNumber,

        @NotBlank @Size(min = 4, max = 10) String expirationDate,

        @NotBlank @Size(min = 3, max = 3) String cvvCode) {

}
