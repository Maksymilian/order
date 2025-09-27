package com.example.order.model

import java.math.BigDecimal
import java.time.Instant

data class Order(
    val createdAt: Instant?,
    val paymentBankAccountNumber: String,
    val totalAmount: BigDecimal,
    val number: String,
    val status: OrderStatus,
    val deductions: List<Deduction>)
