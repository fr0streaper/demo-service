package com.itmo.microservices.demo.order.api.service

import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

interface OrderService {
    fun getOrder(orderId: UUID) : OrderDto?
    fun createOrder(user: UserDetails) : OrderDto
    fun submitOrder(user: UserDetails, orderId: UUID): OrderDto
    fun addItemToBasket(itemId: UUID, orderId: UUID, amount: Int)
    fun bookOrder(orderId: UUID): BookingDto
    fun setDeliverySlot(orderId: UUID, slot: Int)
}
