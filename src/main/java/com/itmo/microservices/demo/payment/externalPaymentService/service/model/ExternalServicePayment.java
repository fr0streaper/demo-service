package com.itmo.microservices.demo.payment.externalPaymentService.service.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponse;
import com.itmo.microservices.demo.payment.externalPaymentService.client.model.ExternalServiceResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExternalServicePayment {

    private UUID id;
    private ExternalServiceResponseStatus status;
    private LocalDateTime submitTime;
    private LocalDateTime completedTime;
    private Long cost;
    private Long delta;

    public static ExternalServicePayment create(ExternalServiceResponse response) {
        return ExternalServicePayment.builder()
                .id(response.getId())
                .status(response.getStatus())
                .submitTime(response.getSubmitTime().toLocalDateTime())
                .completedTime(response.getCompletedTime().toLocalDateTime())
                .cost(response.getCost())
                .delta(response.getDelta())
                .build();
    }
}
