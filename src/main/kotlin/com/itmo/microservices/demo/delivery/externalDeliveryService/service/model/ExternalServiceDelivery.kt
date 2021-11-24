package com.itmo.microservices.demo.delivery.externalDeliveryService.service.model

import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceResponseStatus
import lombok.Data
import java.sql.Timestamp
import java.util.*

@Data
data class ExternalServiceDelivery (
    val id: UUID? = null,
    val title: String = "",
    val status: ExternalServiceResponseStatus? = null,
    val submitTime: Timestamp? = null,
    val completedTime: Timestamp? = null,
    val cost: Long? = null,
    val delta: Long? = null
)