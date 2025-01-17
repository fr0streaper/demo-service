package com.itmo.microservices.demo.lib.common.items.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class CatalogItemEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    var id: UUID? = null
    var title: String = ""
    var description: String = ""
    var price: Int = 100
    var amount: Int = 0

    constructor()

    constructor(id: UUID? = null, title: String, description: String, price: Int, amount: Int) {
        this.id = id
        this.title = title
        this.description = description
        this.price = price
        this.amount = amount
    }
}
