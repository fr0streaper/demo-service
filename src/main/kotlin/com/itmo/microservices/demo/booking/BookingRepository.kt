package com.itmo.microservices.demo.booking

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookingRepository : JpaRepository<BookingEntity, UUID>