package com.itmo.microservices.demo.payment.externalPaymentService.client;

import com.itmo.microservices.demo.payment.PaymentServiceConstants;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceRequest;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponse;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExternalServiceClientImpl implements ExternalServiceClient {

    private final RestTemplate externalPaymentServiceRestTemplate;

    @Override
    public ExternalServiceResponse executePayment(ExternalServiceRequest request) {
        var url = UriComponentsBuilder
                .fromPath(PaymentServiceConstants.EXTERNAL_SERVICE_TRANSACTIONS_URL)
                .build();

        try {
            var response = externalPaymentServiceRestTemplate
                    .postForObject(url.toUriString(), request, ExternalServiceResponse.class);
            if (response == null || response.getStatus() == ExternalServiceResponseStatus.FAILURE) {
                return null; // TODO:: replace with exception
            }

            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn(e.getMessage());
            return null;
        }
    }
}
