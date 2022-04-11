package com.itmo.microservices.demo.delivery.impl.service;

import com.itmo.microservices.demo.delivery.api.service.DeliveryService;
import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDeliveryService {

	DeliveryService deliveryService = new DefaultDeliveryService(new SimpleMeterRegistry());

	@Test
	public void testGetAvailableDeliverySlots() {
		List<Integer> availableDeliverySlots = deliveryService.getAvailableDeliverySlots(1);
		assertThat(availableDeliverySlots.size()).isEqualTo(1);
	}

	@Test
	public void testSetDesiredDeliveryTime() {
		UUID uuid = UUID.randomUUID();
		BookingDto bookingDto = deliveryService.setDesiredDeliveryTime(uuid, 1);
		assertTrue(bookingDto.getFailedItems().isEmpty());
	}
}
