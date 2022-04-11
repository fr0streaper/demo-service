package com.itmo.microservices.demo.internal

import com.itmo.microservices.demo.booking.BookingLogRecord
import com.itmo.microservices.demo.booking.BookingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/_internal")
class InternalController(private val bookingService: BookingService) {

    @GetMapping("/bookingHistory/{bookingId}")
    @Operation(
        summary = "Get booked items by booking id",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()]),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getBookedItems(
        @PathVariable bookingId: UUID,
        @Parameter(hidden = true) @AuthenticationPrincipal requester: UserDetails
    ): List<BookingLogRecord> = bookingService.getBookedItems(bookingId)
}