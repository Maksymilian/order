package com.example.order.service

sealed class InventoryPortResult<out T> {
    data class InventoryPortSuccess<T>(val data: T) : InventoryPortResult<T>()

    data class InventoryPortFailure(val message: String) : InventoryPortResult<Nothing>()
}

data class InventoryPortReserveSuccess(
    val number: String,
)
