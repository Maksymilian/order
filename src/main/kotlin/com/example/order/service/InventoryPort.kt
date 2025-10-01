package com.example.order.service

import reactor.core.publisher.Mono

interface InventoryPort {
    fun reserveStock(reserveStock: ReserveClientRequest): Mono<InventoryPortResult<InventoryPortReserveSuccess>>

    fun cancelReservation(orderNumber: String)
}
