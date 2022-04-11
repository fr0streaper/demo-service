package com.itmo.microservices.demo.booking

import com.itmo.microservices.demo.lib.common.order.entity.OrderEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "booking")
class BookingEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    var id: UUID? = null

    @OneToOne
    var orderEntity: OrderEntity? = null

    constructor()

    constructor(
        id: UUID?,
        orderEntity: OrderEntity?
    ) {
        this.id = id
        this.orderEntity = orderEntity
    }

}
