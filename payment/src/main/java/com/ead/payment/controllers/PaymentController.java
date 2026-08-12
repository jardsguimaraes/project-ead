package com.ead.payment.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentService;
import com.ead.payment.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;

    public PaymentController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{userId}/payments")
    @Transactional
    public ResponseEntity<PaymentModel> requestPayment(@PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid PaymentRequestRecordDto paymentRequestRecordDto) {
        var userModel = userService.findById(userId);
        // inserir validações

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(paymentService.requestPayment(paymentRequestRecordDto, userModel));
    }

}
