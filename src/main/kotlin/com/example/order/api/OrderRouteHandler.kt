package com.example.order.api

import com.example.order.dto.PlaceOrderDTO
import com.example.order.mapper.Mapper
import com.example.order.service.PlaceOrderService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import reactor.core.publisher.Mono
import java.net.URI

@Component
class OrderRouteHandler(val mapper: Mapper, val placeOrderService: PlaceOrderService) {
    fun placeOrder(request: ServerRequest): Mono<ServerResponse> {
        return request
            .bodyToMono(PlaceOrderDTO::class.java)
            .map { mapper.mapToAggregate(it) }
            .flatMap { product -> placeOrderService.placeOrder(product) }
            .map(mapper::mapToDto)
            .flatMap { created -> created(URI.create("http://localhost:8020/test/" + created.number)).bodyValue(created) }
    }
}
