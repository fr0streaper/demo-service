package com.itmo.microservices.demo.payment.api.model;

import java.util.UUID;

public class PaymentSubmissionDto {
    private Long timestamp;
    private UUID transactionId;

    public PaymentSubmissionDto(Long timestamp, UUID transactionId) {
        this.timestamp = timestamp;
        this.transactionId = transactionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }
}
