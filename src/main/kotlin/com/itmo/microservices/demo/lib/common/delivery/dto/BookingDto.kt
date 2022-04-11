package com.itmo.microservices.demo.lib.common.delivery.dto

import java.util.UUID

data class BookingDto (
    val id: UUID,
    val failedItems: Set<UUID>
)
