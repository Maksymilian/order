package com.example.order.model

import java.math.BigDecimal

data class CreateOrder(
    val paymentBankAccountNumber: String,
    val totalAmount: BigDecimal,
    private val _deductions: List<Deduction>,
    val paymentConfirmed: Boolean,
    val number: String?,
    val status: OrderStatus,
) {
    val deductions
        get() = this._deductions.map { it.copy() }
}
