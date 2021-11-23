package com.itmo.microservices.demo.payment.externalPaymentService.service.exception;

public class ExternalServiceException extends Exception {

    public ExternalServiceException(String message) {
        super(message);
    }
}
