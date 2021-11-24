package com.itmo.microservices.demo.delivery.externalDeliveryService.service

import com.itmo.microservices.demo.delivery.externalDeliveryService.client.ExternalServiceClient
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.ExternalServiceClientImpl
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceRequest
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.exception.ExternalServiceException
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.model.ExternalServiceDelivery
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

@Service
class ExternalDeliveryServiceImpl(private val client: ExternalServiceClient): ExternalDeliveryService {
    @Throws(ExternalServiceException::class)
    override fun getDeliveryParams(clientSecret: String): ExternalServiceDelivery {
        val clientRequest = ExternalServiceRequest(clientSecret = clientSecret)
        val clientResponse = client.getDeliveryParams(clientRequest)

        if (clientResponse != null) {
            return ExternalServiceDelivery(
                id = clientResponse.id,
                status = clientResponse.status,
                submitTime = clientResponse.submitTime,
                completedTime = clientResponse.completedTime,
                cost = clientResponse.cost,
                delta = clientResponse.delta
            )
        }
        return ExternalServiceDelivery()
    }
}