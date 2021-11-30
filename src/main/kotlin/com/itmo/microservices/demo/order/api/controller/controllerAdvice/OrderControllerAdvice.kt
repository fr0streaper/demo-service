package com.itmo.microservices.demo.order.api.controller.controllerAdvice

import com.itmo.microservices.demo.lib.common.dto.ErrorResponse
import com.itmo.microservices.demo.payment.api.exception.PaymentServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class OrderControllerAdvice {

    @ExceptionHandler(value = [(PaymentServiceException::class)])
    fun handlePaymentServiceException(e: PaymentServiceException, request: WebRequest): ResponseEntity<ErrorResponse> {

        return ResponseEntity(ErrorResponse(e.message ?: "Unknown error"), HttpStatus.BAD_REQUEST);
    }
}