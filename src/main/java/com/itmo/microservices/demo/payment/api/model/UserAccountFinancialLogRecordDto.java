package com.itmo.microservices.demo.payment.api.model;

import java.util.UUID;

public class UserAccountFinancialLogRecordDto {
    private FinancialOperationType type;
    private Integer amount;
    private UUID orderId;
    private UUID paymentTransactionId;
    private Long timestamp;

    public FinancialOperationType getType() {
        return type;
    }

    public void setType(FinancialOperationType type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(UUID paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
