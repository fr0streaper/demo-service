package com.itmo.microservices.demo.order.api.controller

import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.order.api.service.OrderService
import com.itmo.microservices.demo.payment.api.model.PaymentSubmissionDto
import com.itmo.microservices.demo.payment.api.service.PaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/orders")
class OrderController(private val orderService: OrderService, private val paymentService: PaymentService) {
    @GetMapping("/{order_id}")
    @Operation(
        summary = "Get order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()]),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getOrder(
        @PathVariable order_id: UUID,
        @Parameter(hidden = true) @AuthenticationPrincipal requester: UserDetails
    ): OrderDto? = orderService.getOrder(order_id)

    @PostMapping
    @Operation(
        summary = "Create new order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()]),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun createOrder(@AuthenticationPrincipal user: UserDetails): OrderDto = orderService.createOrder(user)

    @GetMapping("/submit/{order_id}")
    @Operation(
        summary = "Submit order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()]),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun submitOrder(
        @PathVariable("order_id") orderId: UUID,
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserDetails
    ): OrderDto = orderService.submitOrder(user, orderId)

    @PutMapping("/{order_id}/items/{item_id}")
    @Operation(
            summary = "Put item into the basket",
            responses = [
                ApiResponse(description = "OK", responseCode = "200"),
                ApiResponse(description = "Can't put item into the basket", responseCode = "400", content = [Content()]),
                ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()])
            ],
            security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun addItemToBasket(
        @PathVariable("item_id") itemId: UUID,
        @PathVariable("order_id") orderId: UUID,
        @RequestParam amount: Int,
        @Parameter(hidden = true) @AuthenticationPrincipal requester: UserDetails
    ) = orderService.addItemToBasket(itemId, orderId, amount)

    @PostMapping("/{order_id}/payment")
    @Operation(
        summary = "Pay order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()]),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun executeOrderPayment(
        @Parameter(hidden = true)
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable order_id: UUID
    ): PaymentSubmissionDto = paymentService.executeOrderPayment(user, order_id)

    @PostMapping("/{order_id}/bookings")
    @Operation(
        summary = "Book order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()]),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun bookOrder(
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable("order_id") orderId: UUID
    ): BookingDto = orderService.bookOrder(orderId)

    @PostMapping("/{order_id}/delivery")
    @Operation(
        summary = "Set delivery slot",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()]),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun setDeliverySlot(
        @RequestParam slot: Int,
        @AuthenticationPrincipal user: UserDetails,
        @PathVariable("order_id") orderId: UUID
    ) = orderService.setDeliverySlot(orderId, slot)
}