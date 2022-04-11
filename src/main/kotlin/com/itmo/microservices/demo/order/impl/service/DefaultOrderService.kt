package com.itmo.microservices.demo.order.impl.service

import com.itmo.microservices.demo.booking.BookingEntity
import com.itmo.microservices.demo.booking.BookingLogEntity
import com.itmo.microservices.demo.booking.BookingLogRepository
import com.itmo.microservices.demo.booking.BookingRepository
import com.itmo.microservices.demo.booking.BookingStatus
import com.itmo.microservices.demo.items.api.service.WarehouseService
import com.itmo.microservices.demo.lib.common.delivery.dto.BookingDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderItemDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderStatusEnum
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import com.itmo.microservices.demo.lib.common.order.mapper.toEntity
import com.itmo.microservices.demo.lib.common.order.mapper.toModel
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository
import com.itmo.microservices.demo.lib.common.order.repository.OrderRepository
import com.itmo.microservices.demo.order.api.service.OrderService
import com.itmo.microservices.demo.users.api.service.UserService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.fail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.webjars.NotFoundException
import java.util.UUID

@Service
class DefaultOrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val itemService: WarehouseService,
    private val userService: UserService,
    private val meterRegistry: MeterRegistry,
    private val bookingRepository: BookingRepository,
    private val bookingLogRepository: BookingLogRepository
    ): OrderService {

    companion object {
        val log: Logger = LoggerFactory.getLogger(DefaultOrderService::class.java)
    }

    @Value("#{environment['service.name']}")
    val serviceName : String = "";

    private val orderCreatedCount =
        Counter.builder("order_created")
            .description("Count of created orders")

    override fun getOrder(orderId: UUID): OrderDto {
        val optionalOrder = orderRepository.findById(orderId)
        if (optionalOrder.isEmpty) {
            throw NotFoundException("Order with Order ID $orderId not found")
        }
        log.info("Order with Order ID [${orderId}] found")
        return optionalOrder.get().toModel(orderItemRepository)
    }

    override fun createOrder(user: UserDetails): OrderDto {
        val orderEntity = OrderEntity()
        var currentUser = user
        if (currentUser == null) {
            currentUser = SecurityContextHolder.getContext().authentication.principal as UserDetails
        }
        val accountData = userService.getAccountData(currentUser)
        orderEntity.userId = accountData.id

        orderCreatedCount.tag("serviceName", serviceName).register(meterRegistry).increment()

        return orderRepository.save(orderEntity).toModel(orderItemRepository)
    }

    override fun submitOrder(user: UserDetails, orderId: UUID): OrderDto {
        val order = orderRepository.getById(orderId)
        // TODO add check delivery status from delivery service
        order.status = OrderStatusEnum.SHIPPING
        return orderRepository.save(order).toModel(orderItemRepository)
    }

    override fun addItemToBasket(itemId: UUID, orderId: UUID, amount: Int) {
        log.info("Process adding item with id [${itemId}] to order with id [${orderId}]. Amount [${amount}]")
        val item = itemService.getItem(itemId)
        if (item == null) {
            log.info("Item with item_id [$itemId] not found")
            throw NotFoundException("Item with item_id $itemId not found")
        }
        log.info("Item with item_id [$itemId] found")

        val orderEntity = orderRepository.getById(orderId)
        val orderItemId = UUID.randomUUID()
        val orderItemEntity = OrderItemDto(orderItemId, item.title, item.price).toEntity(amount, orderEntity, itemId)
        orderItemRepository.save(orderItemEntity)
        log.info("Saved: Order with id [${orderItemEntity.id}] has [${orderItemEntity.amount}] amount")
    }

    override fun bookOrder(orderId: UUID): BookingDto {
        val optionalOrderEntity = orderRepository.findById(orderId)
        if (optionalOrderEntity.isEmpty) {
            log.info("Order with id [${orderId}] was not found")
            return BookingDto(UUID.randomUUID(), emptySet())
        }

        val orderEntity = optionalOrderEntity.get()
        log.info("Start booking order with id [${orderId}]")

        val bookingId = UUID.randomUUID()
        val bookingEntity = BookingEntity(bookingId, orderEntity)
        val bookingLogEntities: MutableList<BookingLogEntity> = mutableListOf()
        val failedBookingLogEntities: MutableSet<UUID> = mutableSetOf()
        orderEntity.itemsMap?.forEach { orderItemEntity ->
            val item = itemService.getItem(orderItemEntity.itemId!!)
            if (item!!.amount < orderItemEntity.amount!!) {
                failedBookingLogEntities.add(item.id!!)
                bookingLogEntities.add(
                    BookingLogEntity(
                        UUID.randomUUID(),
                        item.id,
                        BookingStatus.FAILED,
                        orderItemEntity.amount,
                        System.currentTimeMillis(),
                        bookingEntity
                    )
                )
            } else {
                bookingLogEntities.add(
                    BookingLogEntity(
                        UUID.randomUUID(),
                        item.id,
                        BookingStatus.SUCCESS,
                        orderItemEntity.amount,
                        System.currentTimeMillis(),
                        bookingEntity
                    )
                )
            }
        }

        log.info("Saving booking with id [${bookingId}] for order with id [${orderId}], failed to book [${failedBookingLogEntities.size}]")
        bookingRepository.save(bookingEntity)

        log.info("Saving [${bookingLogEntities.size}] logs for booking with id [${bookingId}] for order with id [${orderId}]")
        bookingLogRepository.saveAll(bookingLogEntities)

        log.info("Set booked status for order with id [${orderId}], finish booking")
        orderEntity.status = OrderStatusEnum.BOOKED
        orderRepository.save(orderEntity)

        return BookingDto(bookingId, failedBookingLogEntities)
    }

    override fun setDeliverySlot(orderId: UUID, slot: Int) {
        log.info("Setting delivery slot for order with id [${orderId}]")
        val orderEntityOptional = orderRepository.findById(orderId)
        
        if (orderEntityOptional.isEmpty) {
            log.info("Order with id [${orderId}] was not found")
            return
        }

        val orderEntity = orderEntityOptional.get()
        orderEntity.deliveryDuration = slot

        log.info("Saving order with [${orderId}] and delivery slot [${slot}]")
        orderRepository.save(orderEntity)
    }
}
