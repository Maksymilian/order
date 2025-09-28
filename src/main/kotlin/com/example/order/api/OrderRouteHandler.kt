package com.example.order.api

import com.example.order.dto.OrderNotPlacedDTO
import com.example.order.dto.OrderPlacedDTO
import com.example.order.dto.PlaceOrderDTO
import com.example.order.mapper.Mapper
import com.example.order.service.PlaceOrderService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.created
import reactor.core.publisher.Mono
import java.net.URI

@Component
class OrderRouteHandler(val mapper: Mapper, val placeOrderService: PlaceOrderService) {
    fun placeOrder(request: ServerRequest): Mono<ServerResponse> {
        return request
            .bodyToMono(PlaceOrderDTO::class.java)
            .map { mapper.mapToCommand(it) }
            .flatMap { product -> placeOrderService.placeOrder(product) }
            .map(mapper::mapToDto)
            .flatMap { dto ->
//                TODO rozważyć statusy
                when (dto) {
                    is OrderPlacedDTO -> created(URI.create("http://localhost:8020/test/" + dto.number)).bodyValue(dto)
                    is OrderNotPlacedDTO -> badRequest().bodyValue(dto)
                }
            }
    }
}
