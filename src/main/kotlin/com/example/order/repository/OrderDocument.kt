package com.example.order.repository

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.Instant

@Document(collection = "orders")
data class OrderDocument(
    @Id
    val id: ObjectId?,
    @CreatedDate
    val createdAt: Instant?,
    @LastModifiedDate
    val modifiedAt: Instant?,
    @Indexed
    val paymentBankAccountNumber: String,
    @Indexed(unique = true)
    val orderNumber: String,
    val totalAmount: BigDecimal,
    val items: List<Deduction>,
)
