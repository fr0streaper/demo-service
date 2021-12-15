package com.itmo.microservices.demo.payment.externalPaymentService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class ExternalPaymentServiceConfiguration {

    @Value("${external-service.url}")
    private String externalServiceUrl;

    @Bean(name = "externalPaymentServiceRestTemplate")
    public RestTemplate externalPaymentServiceRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .messageConverters(
                        new MappingJackson2HttpMessageConverter(),
                        new StringHttpMessageConverter(),
                        new FormHttpMessageConverter())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .rootUri(externalServiceUrl)
                .build();
    }
}
