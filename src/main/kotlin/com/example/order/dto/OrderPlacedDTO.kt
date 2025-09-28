package com.example.order.dto

import java.time.Instant

sealed interface OrderResultDTO

data class OrderPlacedDTO(
    val id: String,
    val createdAt: Instant,
    val number: String,
) : OrderResultDTO

data class OrderNotPlacedDTO(val reason: String) : OrderResultDTO
