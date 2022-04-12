package com.itmo.microservices.demo.lib.common.order.mapper

import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import com.itmo.microservices.demo.lib.common.order.entity.OrderItemEntity
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository
import com.itmo.microservices.demo.payment.api.model.PaymentLogRecordDto
import com.itmo.microservices.demo.payment.impl.repository.PaymentLogRecordRepository
import java.time.ZoneOffset
import java.util.UUID

fun OrderEntity.toModel(orderItemRepository: OrderItemRepository, paymentLogRecordRepository: PaymentLogRecordRepository): OrderDto = OrderDto(
    id = this.id,
    timeCreated = this.timeCreated.toEpochSecond(ZoneOffset.UTC),
    status = this.status,
    deliveryDuration = this.deliveryDuration,
    itemsMap = this.listToMap(orderItemRepository),
    paymentHistory = this.getPaymentHistory(paymentLogRecordRepository)
)

fun OrderEntity.listToMap(orderItemRepository: OrderItemRepository): Map<UUID, Int> {
    val orderItemEntityList: List<OrderItemEntity>? = orderItemRepository.findByOrderEntity(this)

    return if (orderItemEntityList != null) {
        val orderItemMap: MutableMap<UUID, Int> = mutableMapOf()
        for (orderItem in orderItemEntityList) {
            orderItemMap[orderItem.itemId!!] = orderItem.amount!!
        }
        orderItemMap
    } else {
        mutableMapOf()
    }
}

fun OrderEntity.getPaymentHistory(paymentLogRecordRepository: PaymentLogRecordRepository): List<PaymentLogRecordDto> {
    val optionalPaymentLogs = paymentLogRecordRepository.findAllByOrderEntity(this)

    if (optionalPaymentLogs.isEmpty) {
        return mutableListOf()
    }

    val paymentLogs = optionalPaymentLogs.get()
    if (paymentLogs.isEmpty()) {
        return mutableListOf()
    }

    return paymentLogs.map { paymentLogRecordEntity ->
        PaymentLogRecordDto(
            paymentLogRecordEntity.transactionId,
            paymentLogRecordEntity.timestamp,
            paymentLogRecordEntity.status,
            paymentLogRecordEntity.amount
        )
    }
}
