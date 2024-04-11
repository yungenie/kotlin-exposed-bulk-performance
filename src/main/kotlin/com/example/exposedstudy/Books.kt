package com.example.exposedstudy

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object Payments : LongIdTable(name = "payment") {
    val orderId = long("order_id")
    val amount = decimal("amount", 19, 4)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}

class Payment(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Payment>(Payments)

    var orderId by Payments.orderId
    var amount by Payments.amount
    var createdAt by Payments.createdAt
    var updatedAt by Payments.updatedAt

    override fun toString(): String {
        return "Payment(orderId=$orderId, amount=$amount, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}