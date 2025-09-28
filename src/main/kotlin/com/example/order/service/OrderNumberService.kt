package com.example.order.service

import reactor.core.publisher.Mono

interface OrderNumberService {
    fun generateNextNumber(): Mono<String>
}
