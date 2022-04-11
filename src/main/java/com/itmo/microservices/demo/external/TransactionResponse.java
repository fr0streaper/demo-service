package com.itmo.microservices.demo.external;

import java.util.UUID;

public class TransactionResponse {
	private UUID id;
	private TransactionStatus status;
	private Long submitTime;
	private Long completedTime;
	private Integer cost;
	private Integer delta;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public Long getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}

	public Long getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Long completedTime) {
		this.completedTime = completedTime;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getDelta() {
		return delta;
	}

	public void setDelta(Integer delta) {
		this.delta = delta;
	}
}
