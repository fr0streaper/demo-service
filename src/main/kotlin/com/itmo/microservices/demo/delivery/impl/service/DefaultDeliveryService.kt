package com.itmo.microservices.demo.delivery.impl.service

import com.itmo.microservices.demo.delivery.api.service.DeliveryService
import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class DefaultDeliveryService(private val meterRegistry: MeterRegistry) : DeliveryService {
    @Value("#{environment['service.name']}")
    val serviceName : String = "";

    private val timeslotSetRequestCount =
        Counter.builder("timeslot_set_request_count")
            .description("Count of timeslot set requests")

    override fun getAvailableDeliverySlots(number: Int): List<LocalDateTime> {
        return listOf<LocalDateTime>()
    }

    override fun setDesiredDeliveryTime(order_id: UUID, slot_in_sec: Int): BookingDto {
        timeslotSetRequestCount.tag("serviceName", serviceName).register(meterRegistry).increment()

        return BookingDto(
            id = UUID.randomUUID(),
            failedItems = setOf(),
            orderId = order_id
        )
    }
}
