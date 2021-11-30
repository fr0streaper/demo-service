package com.itmo.microservices.demo.payment.externalPaymentService.service;

import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.externalPaymentService.client.ExternalServiceClient;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceRequest;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponse;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponseStatus;
import com.itmo.microservices.demo.payment.externalPaymentService.service.exception.ExternalServiceException;
import com.itmo.microservices.demo.payment.externalPaymentService.service.model.ExternalServicePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalServiceImpl implements ExternalService {

    private final ExternalServiceClient client;

    private final int maxAttempts = 3;

    @Override
    public ExternalServicePayment executePayment(String clientSecret) throws ExternalServiceException {
        ExternalServiceResponse clientResponse;

        for (int attempt = 1; ; ++attempt) {
            clientResponse = client.executePayment(ExternalServiceRequest.create(clientSecret));

            if (clientResponse == null) {
                if (attempt == maxAttempts) {
                    throw new ExternalServiceException(String.format(
                            "%s Cannot execute payment transaction with client secret: '%s'. Reason: client error. " +
                                    "It was last attempt: %s",
                            PaymentServiceConstants.EXTERNAL_SERVICE_MARKER, clientSecret, attempt));
                } else {
                    log.info("{} Transaction with client secret: {} client error. Attempt: {} of {}. Retrying ...",
                            PaymentServiceConstants.EXTERNAL_SERVICE_MARKER, clientSecret, attempt, maxAttempts);
                }
            } else {
                break;
            }
        }

        if (clientResponse.getStatus() == ExternalServiceResponseStatus.FAILURE) {
            throw new ExternalServiceException(String.format(
                    "%s Cannot execute payment transaction with client secret: '%s'. Reason: transaction failed",
                    PaymentServiceConstants.EXTERNAL_SERVICE_MARKER, clientSecret));
        }

        return ExternalServicePayment.create(clientResponse);
    }
}
