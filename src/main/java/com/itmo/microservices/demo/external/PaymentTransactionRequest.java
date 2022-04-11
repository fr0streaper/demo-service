package com.itmo.microservices.demo.external;

import java.util.UUID;

public class PaymentTransactionRequest {
	private UUID clientSecret;
	private Long sum;

	public UUID getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(UUID clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Long getSum() {
		return sum;
	}

	public void setSum(Long sum) {
		this.sum = sum;
	}
}
