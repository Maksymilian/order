package com.example.order.service

import com.example.order.dto.ReserveDTO
import reactor.core.publisher.Mono

interface InventoryClient {
    fun reserveStock(reserveStock: ReserveDTO): Mono<ClientResponse>
}
