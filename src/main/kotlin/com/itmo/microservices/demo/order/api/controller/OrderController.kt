package com.itmo.microservices.demo.order.api.controller

import com.itmo.microservices.demo.items.api.model.CatalogItem
import com.itmo.microservices.demo.order.api.model.OrderDto
import com.itmo.microservices.demo.tasks.api.service.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/orders/{order_id}")
class OrderController(private val orderService: OrderService) {
    @GetMapping
    @Operation(
        summary = "Get order",
        responses = [
            ApiResponse(description = "OK", responseCode = "200"),
            ApiResponse(description = "Bad request", responseCode = "400", content = [Content()]),
            ApiResponse(description = "Unauthorized", responseCode = "403", content = [Content()])
        ],
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun order(
        @PathVariable orderID: UUID,
        @Parameter(hidden = true) @AuthenticationPrincipal requester: UserDetails
    ): OrderDto {
        return orderService.getOrder(orderID)
    }
}