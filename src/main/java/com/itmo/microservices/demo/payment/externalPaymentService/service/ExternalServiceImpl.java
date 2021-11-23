package com.itmo.microservices.demo.payment.externalPaymentService.service;

import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.externalPaymentService.client.ExternalServiceClient;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceRequest;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponseStatus;
import com.itmo.microservices.demo.payment.externalPaymentService.service.exception.ExternalServiceException;
import com.itmo.microservices.demo.payment.externalPaymentService.service.model.ExternalServicePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExternalServiceImpl implements ExternalService {

    private final ExternalServiceClient client;

    @Override
    public ExternalServicePayment executePayment(String clientSecret) throws ExternalServiceException {
        var clientResponse = client.executePayment(ExternalServiceRequest.create(clientSecret));

        if (clientResponse == null) {
            throw new ExternalServiceException(String.format(
                    "%s Cannot execute payment transaction with client secret: '%s'. Reason: client error",
                    PaymentServiceConstants.EXTERNAL_SERVICE_MARKER, clientSecret));
        }

        if (clientResponse.getStatus() == ExternalServiceResponseStatus.FAILURE) {
            throw new ExternalServiceException(String.format(
                    "%s Cannot execute payment transaction with client secret: '%s'. Reason: transaction failed",
                    PaymentServiceConstants.EXTERNAL_SERVICE_MARKER, clientSecret));
        }

        return ExternalServicePayment.create(clientResponse);
    }
}
