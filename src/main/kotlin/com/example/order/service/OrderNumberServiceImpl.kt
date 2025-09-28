package com.example.order.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class OrderNumberServiceImpl : OrderNumberService {
    override fun generateNextNumber(): Mono<String> {
        TODO("Not yet implemented")
    }
}
