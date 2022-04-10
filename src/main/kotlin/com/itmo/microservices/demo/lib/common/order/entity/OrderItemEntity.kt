package com.itmo.microservices.demo.lib.common.order.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "order_items")
class OrderItemEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    var id: UUID? = null
    var title: String? = null
    var price: Int? = null
    var amount: Int? = null
    var itemId: UUID? = null
    @ManyToOne
    var orderEntity: OrderEntity? = null

    constructor()

    constructor(
        id: UUID?,
        title: String?,
        price: Int?,
        amount: Int?,
        orderEntity: OrderEntity?,
        itemId: UUID?
    ) {
        this.id = id
        this.title = title
        this.price = price
        this.amount = amount
        this.orderEntity = orderEntity
        this.itemId = itemId
    }
}
