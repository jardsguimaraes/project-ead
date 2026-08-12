package com.ead.payment.exeptions;

public class ExternalNotFoundException extends RuntimeException {

    public ExternalNotFoundException(String message) {
        super(message);
    }
}
