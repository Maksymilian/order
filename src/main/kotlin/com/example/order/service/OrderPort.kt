package com.example.order.service

import com.example.order.model.CreateOrder
import reactor.core.publisher.Mono

interface OrderPort {
    fun storeOrder(createOrder: CreateOrder): Mono<OrderPortResult<OrderDocumentSaveSuccess>>

    fun deleteByNumber(number: String)
}
