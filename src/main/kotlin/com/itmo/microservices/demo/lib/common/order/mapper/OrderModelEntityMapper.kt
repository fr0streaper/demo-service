package com.itmo.microservices.demo.lib.common.order.mapper

import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderItemDto
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import com.itmo.microservices.demo.lib.common.order.entity.OrderItemEntity
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository
import com.itmo.microservices.demo.payment.api.model.PaymentLogRecordDto
import java.time.ZoneOffset

fun OrderEntity.toModel(orderItemRepository: OrderItemRepository): OrderDto = OrderDto(
    id = this.id,
    userId = this.userId,
    timeCreated = this.timeCreated.toEpochSecond(ZoneOffset.UTC),
    status = this.status,
    deliveryDuration = this.deliveryDuration,
    itemsMap = this.orderItemsListToMap(orderItemRepository),
    paymentHistory = this.paymentLogRecordEntityToModel()
)

fun OrderEntity.orderItemsListToMap(orderItemRepository: OrderItemRepository): Map<OrderItemDto, Int> {

    //get list of all items bounded with current order
    val orderItemEntityList: List<OrderItemEntity>? = orderItemRepository.findByOrderEntity(this)

    //create empty map for future dto
    val orderItemMap: Map<OrderItemDto, Int> = mutableMapOf()

    //refactor list to map
    if (orderItemEntityList != null) {
        for (orderItem in orderItemEntityList) {
            orderItemMap.plus(Pair(orderItem.toModel(), orderItem.amount))
        }
    }

    return orderItemMap
}

fun OrderEntity.paymentLogRecordEntityToModel(): List<PaymentLogRecordDto> {

    //create empty list for future dto
    val paymentHistoryDto: List<PaymentLogRecordDto> = mutableListOf()

    //convert entities to map
    if (paymentHistory != null) {
        for (paymentLogRecord in paymentHistory!!) {
            paymentHistoryDto.plus(paymentLogRecord.toModel())
        }
    }

    return paymentHistoryDto
}
