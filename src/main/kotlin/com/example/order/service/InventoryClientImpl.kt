package com.example.order.service

import com.example.order.dto.ReserveDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class InventoryClientImpl(val inventoryServiceWebClient: WebClient) : InventoryClient {
    companion object {
        private val logger = LoggerFactory.getLogger(InventoryClientImpl::class.java)
    }

    override fun reserveStock(reserveDTO: ReserveDTO): Mono<ClientResponse> =
        inventoryServiceWebClient
            .post()
            .uri("/reserve")
            .bodyValue(reserveDTO)
            .retrieve()
            .bodyToMono(ClientResponse::class.java)
            .retry(3)
            .onErrorResume { Mono.just(ClientError("Failed to reserve stock.")) }
}
