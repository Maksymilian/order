package com.example.order.model

import java.time.Instant

sealed interface OrderResult

data class OrderPlaced(
    val createdAt: Instant,
    val number: String,
) : OrderResult

data class OrderNotPlaced(val reason: String) : OrderResult
