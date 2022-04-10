package com.itmo.microservices.demo.lib.common.order.mapper

import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import com.itmo.microservices.demo.lib.common.order.entity.OrderItemEntity
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository
import java.time.ZoneOffset
import java.util.UUID

fun OrderEntity.toModel(orderItemRepository: OrderItemRepository): OrderDto = OrderDto(
    id = this.id,
    timeCreated = this.timeCreated.toEpochSecond(ZoneOffset.UTC),
    status = this.status,
    deliveryDuration = this.deliveryDuration,
    itemsMap = this.listToMap(orderItemRepository),
    paymentHistory = emptyList()
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
