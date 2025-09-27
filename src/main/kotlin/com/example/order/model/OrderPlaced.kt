package com.example.order.model

import java.math.BigDecimal
import java.time.Instant

data class OrderPlaced(
    val createdAt: Instant?,
    val number: String)
