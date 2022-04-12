package com.itmo.microservices.demo.payment.impl.repository;

import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity;
import com.itmo.microservices.demo.payment.impl.model.PaymentLogRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentLogRecordRepository extends JpaRepository<PaymentLogRecordEntity, UUID> {
	Optional<List<PaymentLogRecordEntity>> findAllByOrderEntity(OrderEntity orderEntity);
}
