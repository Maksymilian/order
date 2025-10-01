package com.example.order.service

import com.example.order.service.InventoryPortResult.InventoryPortFailure
import com.example.order.service.InventoryPortResult.InventoryPortSuccess
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class InventoryWebClientPort(val inventoryServiceWebClient: WebClient) : InventoryPort {
    companion object {
        private val logger = LoggerFactory.getLogger(InventoryWebClientPort::class.java)
    }

    override fun reserveStock(reserveClientRequest: ReserveClientRequest): Mono<InventoryPortResult<InventoryPortReserveSuccess>> =
        inventoryServiceWebClient
            .post()
            .uri("/reserve")
            .bodyValue(reserveClientRequest)
            .retrieve()
            .bodyToMono(
                InventoryPortReserveSuccess::class.java,
            )
            .retry(3)
            .map<InventoryPortResult<InventoryPortReserveSuccess>> { InventoryPortSuccess(it) }
            .onErrorResume { Mono.just(InventoryPortFailure("Failed to reserve stock.")) }

    override fun cancelReservation(orderNumber: String) {
        TODO("Not yet implemented")
    }
}
