package com.example.order.model

import java.math.BigDecimal
import java.time.Instant

data class CreateOrder(
    val createdAt: Instant?,
    val paymentBankAccountNumber: String,
    val totalAmount: BigDecimal,
    val status: OrderStatus,
    val deductions: List<Deduction>)
