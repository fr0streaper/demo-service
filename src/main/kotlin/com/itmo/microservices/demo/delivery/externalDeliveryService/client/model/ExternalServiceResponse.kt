package com.itmo.microservices.demo.delivery.externalDeliveryService.client.model

import java.sql.Timestamp
import java.util.UUID

data class ExternalServiceResponse(
    val id: UUID,
    val status: ExternalServiceResponseStatus,
    val submitTime: Timestamp,
    val completedTime: Timestamp?,
    val cost: Long?,
    val delta: Long
)