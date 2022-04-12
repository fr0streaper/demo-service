package com.itmo.microservices.demo.payment.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountFinancialLogRecordDto {
    private FinancialOperationType type;
    private Integer amount;
    private UUID orderId;
    private UUID paymentTransactionId;
    private Long timestamp;
}
