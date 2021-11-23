package com.itmo.microservices.demo.payment.externalPaymentService.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

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
