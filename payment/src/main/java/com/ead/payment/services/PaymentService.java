package com.ead.payment.services;

import com.ead.payment.dtos.PaymentRequestRecordDto;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.models.UserModel;

public interface PaymentService {

    PaymentModel requestPayment(PaymentRequestRecordDto paymentRequestRecordDto, UserModel userModel);

}
