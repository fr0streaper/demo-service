package com.itmo.microservices.demo.payment.impl.model;

import com.itmo.microservices.demo.payment.api.model.FinancialOperationType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public final class UserAccountFinancialLogRecord {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    @Column(columnDefinition = "uuid")
    private UUID orderId; //TODO:: replace with related  entity
    @Column(columnDefinition = "uuid")
    private UUID paymentTransactionId; //TODO:: replace with related entity

    @Enumerated(EnumType.STRING)
    private FinancialOperationType type;
    private Integer amount;
    private Long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserAccountFinancialLogRecord that = (UserAccountFinancialLogRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public UserAccountFinancialLogRecord(UUID id, UUID orderId, UUID paymentTransactionId, FinancialOperationType type, Integer amount, Long timestamp) {
        this.id = id;
        this.orderId = orderId;
        this.paymentTransactionId = paymentTransactionId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
