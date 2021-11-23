package com.itmo.microservices.demo.payment.externalPaymentService.client.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExternalServiceResponse {

    private UUID id;
    private ExternalServiceResponseStatus status;
    private Timestamp submitTime; // TODO:: make up custom (de)serializer to Date or LocalDateTime
    private Timestamp completedTime;
    private Long cost;
    private Long delta;
}
