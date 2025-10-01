package com.example.order.mapper

import com.example.order.dto.OrderNotPlaceResponse
import com.example.order.dto.OrderPlacedResponse
import com.example.order.dto.PlaceOrderRequest
import com.example.order.model.CreateOrder
import com.example.order.model.OrderNotPlaced
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderResult
import com.example.order.model.OrderStatus
import com.example.order.repository.OrderDocument
import com.example.order.service.ReserveClientRequest
import org.springframework.stereotype.Component
import com.example.order.model.Deduction as DeductionEntity
import com.example.order.repository.Deduction as DeductionDocument

// TODO possible enhancement: implement with MapStruct
@Component
class Mapper {
    fun mapToCommand(placeOrder: PlaceOrderRequest) =
        with(placeOrder) {
            CreateOrder(
                paymentConfirmed = false,
                number = null,
                status = OrderStatus.NEW,
                paymentBankAccountNumber = paymentBankAccountNumber,
                totalAmount = totalAmount,
                _deductions = deductions.map { DeductionEntity(sku = it.sku, quantity = it.quantity) },
            )
        }

    fun mapToDto(order: OrderResult) =
        when (order) {
            is OrderPlaced -> OrderPlacedResponse(createdAt = order.createdAt, number = order.number)
            is OrderNotPlaced -> OrderNotPlaceResponse(reason = order.reason)
        }

    fun mapToMongoDocument(order: CreateOrder) =
        with(order) {
            OrderDocument(
                id = null,
                createdAt = null,
                modifiedAt = null,
                paymentBankAccountNumber = paymentBankAccountNumber,
                orderNumber = requireNotNull(number),
                totalAmount = totalAmount,
                items = deductions.map { with(it) { DeductionDocument(sku = sku, quantity = quantity) } },
            )
        }

    fun maptoReserveStockRequest(createOrder: CreateOrder) =
        ReserveClientRequest(
            requireNotNull(createOrder.number),
            createOrder.deductions,
        )
}
