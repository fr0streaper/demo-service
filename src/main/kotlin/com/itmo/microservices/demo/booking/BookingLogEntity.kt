package com.itmo.microservices.demo.booking

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "booking_log")
class BookingLogEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    var id: UUID? = null

    var itemId: UUID? = null

    @Enumerated(EnumType.STRING)
    var status: BookingStatus? = null

    var amount: Int? = null

    var timestamp: Long? = null

    @ManyToOne
    var bookingEntity: BookingEntity? = null

    constructor()

    constructor(
        id: UUID?,
        itemId: UUID?,
        status: BookingStatus?,
        amount: Int?,
        timestamp: Long?,
        bookingEntity: BookingEntity?
    ) {
        this.id = id
        this.itemId = itemId
        this.status = status
        this.amount = amount
        this.timestamp = timestamp
        this.bookingEntity = bookingEntity
    }

}
