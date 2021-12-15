package com.itmo.microservices.demo.payment.externalPaymentService.service;

import com.itmo.microservices.demo.payment.externalPaymentService.service.exception.ExternalServiceException;
import com.itmo.microservices.demo.payment.externalPaymentService.service.model.ExternalServicePayment;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface ExternalService {

    CompletableFuture<ExternalServicePayment> executePayment(String clientSecret) throws CompletionException;
}
