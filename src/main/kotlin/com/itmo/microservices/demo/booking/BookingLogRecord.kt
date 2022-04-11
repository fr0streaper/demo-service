package com.itmo.microservices.demo.booking

import java.util.UUID

data class BookingLogRecord (
    var bookingId: UUID?,
    var itemId: UUID?,
    var status: BookingStatus?,
    var amount: Int?,
    var timestamp: Long?
)
