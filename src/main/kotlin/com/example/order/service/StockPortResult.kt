package com.example.order.service

sealed class StockPortResult<out T> {
    data class StockPortSuccess<T>(val data: T) : StockPortResult<T>()

    data class StockPortFailure(val message: String) : StockPortResult<Nothing>()
}

class StockTransportRequestSuccess()

class StockTransportCancelledSuccess()
