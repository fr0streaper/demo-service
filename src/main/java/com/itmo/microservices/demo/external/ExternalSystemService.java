package com.itmo.microservices.demo.external;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ExternalSystemService {

	private final RestTemplate restTemplate;

	public ExternalSystemService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public TransactionResponse getPaymentTransactionResponse(Long sum) {
		final String url = "http://tps:8080/transactions";
		final UUID clientSecret = UUID.fromString("9db11709-6871-4660-996b-398ee440f0a6");

		Map<String, Object> map = new HashMap<>();
		map.put("clientSecret", clientSecret);
		map.put("sum", sum);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

		ResponseEntity<TransactionResponse> response = this.restTemplate.postForEntity(url, entity, TransactionResponse.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		} else {
			return null;
		}
	}
}
