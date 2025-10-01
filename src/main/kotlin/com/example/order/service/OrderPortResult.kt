package com.example.order.service

import java.time.Instant

sealed class OrderPortResult<out T> {
    data class OrderPortResultSuccess<T>(val data: T) : OrderPortResult<T>()

    data class OrderPortResultFailure(val message: String) : OrderPortResult<Nothing>()
}

data class OrderDocumentSaveSuccess(
    val id: String,
    val createdAt: Instant,
    val number: String,
)
