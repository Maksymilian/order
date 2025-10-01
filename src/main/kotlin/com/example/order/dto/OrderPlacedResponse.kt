package com.example.order.dto

import java.time.Instant

sealed interface OrderResultResponse

data class OrderPlacedResponse(
    val createdAt: Instant,
    val number: String,
) : OrderResultResponse

data class OrderNotPlaceResponse(val reason: String) : OrderResultResponse
