package com.ead.payment.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.payment.configs.security.AuthenticationCurrentUserService;
import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentService;
import com.ead.payment.services.UserService;
import com.ead.payment.specifications.SpecificationTemplate;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class PaymentController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    public PaymentController(UserService userService, PaymentService paymentService,
            AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/{userId}/payments")
    @Transactional
    public ResponseEntity<Object> requestPayment(@PathVariable(value = "userId") UUID userId,
            @RequestBody @Valid PaymentRequestRecordDto paymentRequestRecordDto) {
        var userModel = userService.findById(userId);

        Optional<PaymentModel> paymentModelOptinal = paymentService.findLastPaymentByUser(userModel);

        if (paymentModelOptinal.isPresent()) {
            if (paymentModelOptinal.get().getPaymentControl().equals(PaymentControl.REQUESTED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already request.");
            }

            if (paymentModelOptinal.get().getPaymentControl().equals(PaymentControl.EFFECTED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment already made");
            }
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(paymentService.requestPayment(paymentRequestRecordDto, userModel));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}/payments")
    public ResponseEntity<Page<PaymentModel>> getAllPayments(@PathVariable(value = "userId") UUID userId,
            SpecificationTemplate.PaymentSpec spec, Pageable pageable) {
        var userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(paymentService.findAllByUser(SpecificationTemplate.paymentUserId(userId).and(spec),
                            pageable));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}/payments/{paymentId}")
    public ResponseEntity<PaymentModel> getOnePayment(@PathVariable(value = "userId") UUID userId,
            @PathVariable(value = "paymentId") UUID paymentId) {
        var userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userId)
                || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK).body(paymentService.findPaymentByUser(userId, paymentId));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
