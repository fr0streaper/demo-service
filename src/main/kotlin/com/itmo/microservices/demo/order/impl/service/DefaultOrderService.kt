package com.itmo.microservices.demo.order.impl.service

import com.itmo.microservices.demo.items.api.service.WarehouseService
import com.itmo.microservices.demo.lib.common.order.dto.OrderDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderItemDto
import com.itmo.microservices.demo.lib.common.order.dto.OrderStatusEnum
import com.itmo.microservices.demo.order.api.service.OrderService
import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import com.itmo.microservices.demo.lib.common.order.mapper.toEntity
import com.itmo.microservices.demo.lib.common.order.repository.OrderItemRepository
import com.itmo.microservices.demo.lib.common.order.mapper.toModel
import com.itmo.microservices.demo.lib.common.order.repository.OrderRepository
import com.itmo.microservices.demo.tasks.impl.messaging.TaskModuleEventListener
import com.itmo.microservices.demo.users.api.service.UserService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import java.util.*
import org.webjars.NotFoundException

@Service
class DefaultOrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val itemService: WarehouseService,
    private val userService: UserService,
    private val meterRegistry: MeterRegistry
    ): OrderService {

    companion object {
        val log: Logger = LoggerFactory.getLogger(TaskModuleEventListener::class.java)
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
        } else {
            log.info("Item with item_id [$itemId] found")
        }
        val orderEntity = orderRepository.getById(orderId)
        val orderItemId = UUID.randomUUID()
        val orderItemEntity = OrderItemDto(orderItemId, item.title, item.price).toEntity(amount, orderEntity)
        orderItemRepository.save(orderItemEntity)
        log.info("Saved: Order with id [${orderItemEntity.id}] has [${orderItemEntity.amount}] amount")
        val savedOrderItemEntity = orderItemRepository.findById(orderItemId)
        if (savedOrderItemEntity.isEmpty) {
            log.error("OrderItem with id [$orderItemId] not found")
        } else {
            log.info("Got: Order with id [${savedOrderItemEntity.get().id}] has [${savedOrderItemEntity.get().amount}] amount")
        }
    }
}
