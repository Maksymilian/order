package com.example.order.service

import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.model.OrderNotPlaced
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderResult
import com.example.order.service.InventoryPortResult.InventoryPortFailure
import com.example.order.service.InventoryPortResult.InventoryPortSuccess
import com.example.order.service.OrderPortResult.OrderPortResultFailure
import com.example.order.service.OrderPortResult.OrderPortResultSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class PlaceOrderService(
    val mapper: Mapper,
    val orderPort: OrderPort,
    val orderNumberService: OrderNumberService,
    val inventoryPort: InventoryPort,
) {
    fun placeOrder(createOrder: CreateOrder): Mono<OrderResult> =

        orderNumberService.generateNextNumber()
            .map { number -> createOrder.copy(number = number) }
            .flatMap { createOrder ->
                val databaseWrite = orderPort.storeOrder(createOrder)
                val clientCall = inventoryPort.reserveStock(mapper.maptoReserveStockRequest(createOrder))
                Mono.zip(clientCall, databaseWrite)
            }.flatMap { (clientCall, databaseWrite) ->
                when {
                    clientCall is InventoryPortSuccess<*> && databaseWrite is OrderPortResultSuccess<OrderDocumentSaveSuccess> -> {
                        Mono.just(OrderPlaced(number = databaseWrite.data.number, createdAt = databaseWrite.data.createdAt))
                    }
                    clientCall is InventoryPortFailure && databaseWrite is OrderPortResultSuccess<OrderDocumentSaveSuccess> -> {
                        orderPort.deleteByNumber(databaseWrite.data.number)
                        Mono.just(OrderNotPlaced(reason = "Inventory service problem"))
                    }
                    clientCall is InventoryPortSuccess<InventoryPortReserveSuccess> && databaseWrite is OrderPortResultFailure -> {
                        inventoryPort.cancelReservation(clientCall.data.number)
                        Mono.just(OrderNotPlaced(reason = "Order database problem"))
                    }
                    else -> {
                        Mono.just(OrderNotPlaced(reason = "Inventory service error"))
                    }
                }
            }
}
