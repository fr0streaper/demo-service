package com.itmo.microservices.demo.lib.common.order.mapper

import com.itmo.microservices.demo.lib.common.order.dto.OrderItemDto
import com.itmo.microservices.demo.lib.common.order.entity.OrderItemEntity
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import java.util.UUID

fun OrderItemDto.toEntity(amountFromOrderDto: Int?, orderFromOrderEntityDto: OrderEntity?, itemId: UUID): OrderItemEntity = OrderItemEntity(
    id = this.id,
    title = this.title,
    price = this.price,
    amount = amountFromOrderDto,
    orderEntity = orderFromOrderEntityDto,
    itemId = itemId
)

fun OrderItemEntity.toModel(): OrderItemDto = OrderItemDto(
    id = this.id,
    title = this.title,
    price = this.price
)
