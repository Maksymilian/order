package com.example.order.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface OrdersRepository : ReactiveMongoRepository<OrderDocument, String>
