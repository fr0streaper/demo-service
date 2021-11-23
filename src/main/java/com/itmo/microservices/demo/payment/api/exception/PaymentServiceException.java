package com.itmo.microservices.demo.payment.api.exception;

public class PaymentServiceException extends Exception {

    public PaymentServiceException(String message) {
        super(message);
    }
}
