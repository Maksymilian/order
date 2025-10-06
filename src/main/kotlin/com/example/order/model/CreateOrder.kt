package com.example.order.model

import java.math.BigDecimal

data class CreateOrder(
    val paymentBankAccountNumber: String,
    val totalAmount: BigDecimal,
    val number: String?,
    private val _deductions: List<Deduction>,
) {
    val deductions
        get() = this._deductions.map { it.copy() }
}
