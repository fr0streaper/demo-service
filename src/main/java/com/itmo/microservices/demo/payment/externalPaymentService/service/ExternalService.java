package com.itmo.microservices.demo.payment.externalPaymentService.service;

import com.itmo.microservices.demo.payment.externalPaymentService.service.exception.ExternalServiceException;
import com.itmo.microservices.demo.payment.externalPaymentService.service.model.ExternalServicePayment;

public interface ExternalService {

    ExternalServicePayment executePayment(String clientSecret) throws ExternalServiceException;
}
