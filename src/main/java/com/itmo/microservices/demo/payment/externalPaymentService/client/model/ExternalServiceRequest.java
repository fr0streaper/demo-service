package com.itmo.microservices.demo.payment.externalPaymentService.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExternalServiceRequest {

    private String clientSecret;

    public static ExternalServiceRequest create(String clientSecret) {
        return new ExternalServiceRequest(clientSecret);
    }
}
