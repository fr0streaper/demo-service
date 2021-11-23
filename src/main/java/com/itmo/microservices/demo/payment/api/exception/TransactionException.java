package com.itmo.microservices.demo.payment.api.exception;

public class TransactionException extends PaymentServiceException {

    public TransactionException(String message) {
        super(message);
    }
}
