package com.itmo.microservices.demo.delivery.externalDeliveryService.client

import com.itmo.microservices.demo.delivery.DeliveryServiceConstants
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceRequest
import com.itmo.microservices.demo.delivery.externalDeliveryService.client.model.ExternalServiceResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject
import org.springframework.web.util.UriComponentsBuilder

@Component
class ExternalServiceClientImpl(val externalDeliveryServiceRestTemplate: RestTemplate): ExternalServiceClient {

    override fun getDeliveryParams(request: ExternalServiceRequest): ExternalServiceResponse? {
        val url = UriComponentsBuilder
            .fromPath(DeliveryServiceConstants.EXTERNAL_SERVICE_TRANSACTIONS_URL)
            .build()

        return try {
            externalDeliveryServiceRestTemplate
                .postForObject(url.toUriString(), request, ExternalServiceResponse::class)
        } catch (e: HttpClientErrorException) {
            null
        } catch (e: HttpServerErrorException) {
            null
        }
    }
}