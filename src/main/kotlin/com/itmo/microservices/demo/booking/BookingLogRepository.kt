package com.itmo.microservices.demo.booking

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookingLogRepository : JpaRepository<BookingLogEntity, UUID> {
    fun findByBookingEntity(bookingEntity: BookingEntity): List<BookingLogEntity>
}
