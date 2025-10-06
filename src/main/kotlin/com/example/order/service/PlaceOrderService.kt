package com.example.order.service

import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.model.OrderNotPlaced
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderResult
import com.example.order.service.OrderPortResult.OrderPortResultFailure
import com.example.order.service.OrderPortResult.OrderPortResultSuccess
import com.example.order.service.StockPortResult.StockPortFailure
import com.example.order.service.StockPortResult.StockPortSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.kotlin.core.util.function.component3

@Service
class PlaceOrderService(
    val mapper: Mapper,
    val orderPort: OrderPort,
    val orderNumberService: OrderNumberService,
    val stockPort: StockPort,
) {
    fun placeOrder(createOrder: CreateOrder): Mono<OrderResult> {
        return orderNumberService.generateNextNumber()
            .map { number -> createOrder.copy(number = number) }
            .flatMap { createOrder ->
                val databaseWrite = orderPort.storeOrder(createOrder)
                val clientCall = stockPort.reserveStock(mapper.maptoReserveStockRequest(createOrder))
                val order: Mono<CreateOrder> = Mono.just(createOrder)
                Mono.zip(clientCall, databaseWrite, order)
            }.flatMap { (clientCall, databaseWrite, order) ->
//                TODO:possible enhancement - replace with a  queue
                when {
                    clientCall is StockPortSuccess<*> && databaseWrite is OrderPortResultSuccess<OrderDocumentSaveSuccess> -> {
                        Mono.just(
                            OrderPlaced(
                                number = requireNotNull(order.number),
                                createdAt = databaseWrite.data.createdAt,
                            ),
                        )
                    }

                    clientCall is StockPortFailure && databaseWrite is OrderPortResultSuccess<OrderDocumentSaveSuccess> -> {
                        orderPort.deleteByNumber(requireNotNull(order.number))
                        Mono.just(OrderNotPlaced(reason = "Inventory service problem"))
                    }

                    clientCall is StockPortSuccess<StockTransportRequestSuccess> && databaseWrite is OrderPortResultFailure -> {
                        stockPort.cancelTransport(order.deductions)
                        Mono.just(OrderNotPlaced(reason = "Order database problem"))
                    }

                    else -> {
                        Mono.just(OrderNotPlaced(reason = "Inventory service error"))
                    }
                }
            }
    }
}
//        TODO zamówienie po 24h pół godziny w statusie placed jest odwołane
//        TODO na koniec dnia (23:00) jest sprawdzenie czy jest prepared
