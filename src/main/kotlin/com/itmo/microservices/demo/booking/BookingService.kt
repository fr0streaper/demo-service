package com.itmo.microservices.demo.booking

import java.util.UUID

interface BookingService {
    fun getBookedItems(bookingId: UUID) : List<BookingLogRecord>
}