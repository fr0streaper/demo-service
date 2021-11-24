package com.itmo.microservices.demo.delivery.externalDeliveryService.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ExternalDeliveryServiceConfiguration {
    @Value("\${external-service.url}")
    private val externalServiceUrl: String = ""

    @Bean(name = ["externalPaymentServiceRestTemplate"])
    fun externalDeliveryServiceRestTemplate(): RestTemplate? {
        return RestTemplateBuilder()
            .messageConverters(
                MappingJackson2HttpMessageConverter(),
                StringHttpMessageConverter(),
                FormHttpMessageConverter()
            )
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .rootUri(externalServiceUrl)
            .build()
    }
}