package com.itmo.microservices.demo.delivery.impl.service

import com.itmo.microservices.demo.delivery.api.service.DeliveryService
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.ExternalDeliveryService
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.model.ExternalServiceDelivery
import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DefaultDeliveryService(private val externalDeliveryService: ExternalDeliveryService) : DeliveryService {
    @Value("\${external-service.delivery-client-secret}")
    private val clientSecret: String = ""

    override fun getAvailableDeliverySlots(): ExternalServiceDelivery {
        return externalDeliveryService.getDeliveryParams(clientSecret)
    }

    override fun setDesiredDeliveryTime(order_id: UUID, slot_in_sec: Int): BookingDto {
        return BookingDto(
            id = UUID.randomUUID(),
            failedItems = setOf(),
            orderId = order_id
        )
    }
}
