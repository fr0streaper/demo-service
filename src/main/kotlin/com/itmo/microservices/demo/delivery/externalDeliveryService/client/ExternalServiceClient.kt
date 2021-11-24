package com.itmo.microservices.demo.delivery.externalDeliveryService.client

import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceRequest
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceResponse

interface ExternalServiceClient {
    fun getDeliveryParams(request: ExternalServiceRequest): ExternalServiceResponse?
}