package com.itmo.microservices.demo.lib.common.order.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.itmo.microservices.demo.payment.api.model.PaymentLogRecordDto
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrderDto(
    var id: UUID?,
    var timeCreated: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    var status: OrderStatusEnum = OrderStatusEnum.COLLECTING,
    var itemsMap: Map<UUID, Int>?,
    var deliveryDuration: Int?,
    var paymentHistory: List<PaymentLogRecordDto>?
)
