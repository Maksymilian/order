package com.example.order.service

import com.example.order.model.Deduction
import reactor.core.publisher.Mono

interface StockPort {
    fun reserveStock(reserveStock: ReserveClientRequest): Mono<StockPortResult<StockTransportRequestSuccess>>

    fun cancelTransport(orderNumber: List<Deduction>): Mono<StockPortResult<StockTransportCancelledSuccess>>
}
