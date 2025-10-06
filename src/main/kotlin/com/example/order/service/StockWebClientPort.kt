package com.example.order.service

import com.example.order.model.Deduction
import com.example.order.service.StockPortResult.StockPortFailure
import com.example.order.service.StockPortResult.StockPortSuccess
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class StockWebClientPort(val inventoryServiceWebClient: WebClient) : StockPort {
    companion object {
        private val logger = LoggerFactory.getLogger(StockWebClientPort::class.java)
    }

    override fun reserveStock(reserveClientRequest: ReserveClientRequest): Mono<StockPortResult<StockTransportRequestSuccess>> =
        inventoryServiceWebClient
            .post()
            .uri("/requestTransport")
            .bodyValue(reserveClientRequest)
            .retrieve()
            .toBodilessEntity()
            .retry(3)
            .map<StockPortResult<StockTransportRequestSuccess>> { StockPortSuccess(StockTransportRequestSuccess()) }
            .onErrorResume { Mono.just(StockPortFailure("Failed to request transport.")) }

    override fun cancelTransport(deductions: List<Deduction>) =
        inventoryServiceWebClient
            .post()
            .uri("/cancelTransport")
            .bodyValue(deductions)
            .retrieve()
            .toBodilessEntity()
            .retry(3)
            .map<StockPortResult<StockTransportCancelledSuccess>> { StockPortSuccess(StockTransportCancelledSuccess()) }
            .onErrorResume { Mono.just(StockPortFailure("Failed to cancel transport.")) }
}
