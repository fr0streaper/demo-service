package com.itmo.microservices.demo.delivery.externalDeliveryService.service

import com.itmo.microservices.demo.delivery.externalDeliveryService.service.exception.ExternalServiceException
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.model.ExternalServiceDelivery

interface ExternalDeliveryService {
    @Throws(ExternalServiceException::class)
    fun getDeliveryParams(clientSecret: String): ExternalServiceDelivery
}
