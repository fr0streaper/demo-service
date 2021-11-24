package com.itmo.microservices.demo.delivery.externalDeliveryService.service

import com.itmo.microservices.demo.delivery.externalDeliveryService.client.ExternalServiceClient
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceRequest
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceResponse
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceResponseStatus
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.exception.ExternalServiceException
import com.itmo.microservices.demo.delivery.externalDeliveryService.service.model.ExternalServiceDelivery
import org.springframework.stereotype.Service

@Service
class ExternalDeliveryServiceImpl(private val client: ExternalServiceClient) : ExternalDeliveryService {

    @Throws(ExternalServiceException::class)
    override fun getDeliveryParams(clientSecret: String): ExternalServiceDelivery {
        val clientRequest = ExternalServiceRequest(clientSecret = clientSecret)
        var clientResponse = client.getDeliveryTransactionId(clientRequest)

        if (clientResponse != null && clientResponse.status == ExternalServiceResponseStatus.PENDING) {
            clientResponse = keepPolling(clientRequest, clientResponse.id.toString())
        } else if (clientResponse == null || clientResponse.status == ExternalServiceResponseStatus.FAILURE) {
            getDeliveryParams(clientSecret)
        }

        if (clientResponse != null && clientResponse.status == ExternalServiceResponseStatus.SUCCESS) {
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

    // TODO replace with Integration Flow poller
    private fun keepPolling(request: ExternalServiceRequest, transactionId: String): ExternalServiceResponse? {
        while (true) {
            Thread.sleep(5000)
            val clientResponse = client.getDeliveryParams(request, transactionId)
            if (clientResponse?.status != ExternalServiceResponseStatus.PENDING) {
                return clientResponse
            }
        }
    }

}