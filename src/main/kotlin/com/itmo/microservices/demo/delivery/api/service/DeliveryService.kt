package com.itmo.microservices.demo.delivery.api.service

import com.itmo.microservices.demo.delivery.externalDeliveryService.service.model.ExternalServiceDelivery
import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import java.time.LocalDateTime
import java.util.*

interface DeliveryService {
    fun getAvailableDeliverySlots(number: Int): /*List<LocalDateTime>*/ ExternalServiceDelivery
    fun setDesiredDeliveryTime(order_id: UUID, slot_in_sec: Int): BookingDto
}
