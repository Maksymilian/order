package com.example.order.service

import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.repository.OrdersRepository
import com.example.order.service.OrderPortResult.OrderPortResultFailure
import com.example.order.service.OrderPortResult.OrderPortResultSuccess
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class OrderMongoPort(val ordersRepository: OrdersRepository, val mapper: Mapper) : OrderPort {
    companion object {
        private val logger = LoggerFactory.getLogger(OrderMongoPort::class.java)
    }

    override fun storeOrder(createOrder: CreateOrder): Mono<OrderPortResult<OrderDocumentSaveSuccess>> =
        ordersRepository.save(mapper.mapToMongoDocument(createOrder))
            .map<OrderPortResult<OrderDocumentSaveSuccess>> {
                OrderPortResultSuccess(
                    OrderDocumentSaveSuccess(
                        id = checkNotNull(it.id).toHexString(),
                        createdAt = checkNotNull(it.createdAt),
                        number = it.orderNumber,
                    ),
                )
            }
            .onErrorResume(Exception::class.java) {
                logger.error("Failed to store an  order", it)
                Mono.just(
                    OrderPortResultFailure(
                        message = it.message ?: "",
                    ),
                )
            }

    override fun deleteByNumber(number: String) {
        TODO("Not yet implemented")
    }
}
