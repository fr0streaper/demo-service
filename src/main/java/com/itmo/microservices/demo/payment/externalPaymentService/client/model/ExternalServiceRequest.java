package com.itmo.microservices.demo.payment.externalPaymentService.client.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
