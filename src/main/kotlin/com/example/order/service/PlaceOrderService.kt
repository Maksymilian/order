package com.example.order.service

import com.example.order.dto.ReserveDTO
import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.model.OrderResult
import com.example.order.repository.OrdersRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PlaceOrderService(
    val mapper: Mapper,
    val ordersRepository: OrdersRepository,
    val orderNumberService: OrderNumberService,
    val inventoryClient: InventoryClient,
) {
    fun placeOrder(product: CreateOrder): Mono<OrderResult> =
        Mono.just(product)
            .flatMap { createOrder ->
                orderNumberService.generateNextNumber().map { number -> createOrder.copy(number = number) }
            }
            .flatMap { createOrder ->
                val reserveStock = ReserveDTO(requireNotNull(createOrder.number), createOrder.deductions)
                val orderDocument = mapper.mapToMongoDocument(createOrder)
                Mono.zip(
                    inventoryClient.reserveStock(reserveStock),
                    ordersRepository.save(orderDocument),
                )
                    .flatMap { Mono.empty<OrderResult>() }
            }
}
