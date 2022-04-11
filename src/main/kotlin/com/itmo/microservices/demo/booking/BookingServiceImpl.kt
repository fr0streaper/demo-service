package com.itmo.microservices.demo.booking

import com.itmo.microservices.demo.order.impl.service.DefaultOrderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BookingServiceImpl(
    private val bookingLogRepository: BookingLogRepository,
    private val bookingRepository: BookingRepository
) : BookingService {

    companion object {
        val log: Logger = LoggerFactory.getLogger(DefaultOrderService::class.java)
    }

    override fun getBookedItems(bookingId: UUID): List<BookingLogRecord> {
        log.info("Getting booked items for booking with id [${bookingId}]")
        val bookingEntity = bookingRepository.findById(bookingId)
        if (bookingEntity.isEmpty) {
            log.info("Booking with id [${bookingId}] was not found")
            return emptyList()
        }

        log.info("Booking with id [${bookingId}] was found, start finding booked items")
        val bookingLogEntityList = bookingLogRepository.findByBookingEntity(bookingEntity.get())
        if (bookingLogEntityList.isEmpty()) {
            log.info("Booked items for booking with id [${bookingId}] was not found")
            return emptyList()
        }

        return bookingLogEntityList.map { bookingLogEntity -> bookingLogEntityToDto(bookingLogEntity, bookingId) }
    }

    private fun bookingLogEntityToDto(bookingLogEntity: BookingLogEntity, bookingId: UUID): BookingLogRecord =
        BookingLogRecord(
            bookingId = bookingId,
            itemId = bookingLogEntity.itemId,
            status = bookingLogEntity.status,
            amount = bookingLogEntity.amount,
            timestamp = bookingLogEntity.timestamp
        )
}