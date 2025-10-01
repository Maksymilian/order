package com.example.order.dto

import java.math.BigDecimal
import java.time.Instant

data class PlaceOrderRequest(
    val createdAt: Instant?,
    val paymentBankAccountNumber: String,
    val totalAmount: BigDecimal,
    val deductions: List<DeductionRequest>,
)
