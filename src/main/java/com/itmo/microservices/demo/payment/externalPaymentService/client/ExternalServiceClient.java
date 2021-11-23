package com.itmo.microservices.demo.payment.externalPaymentService.client;

import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceRequest;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponse;

public interface ExternalServiceClient {

    ExternalServiceResponse executePayment(ExternalServiceRequest request);
}
