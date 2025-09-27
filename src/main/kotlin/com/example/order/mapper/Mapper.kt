package com.example.order.mapper

import com.example.order.dto.PlaceOrderDTO
import com.example.order.model.CreateOrder
import com.example.order.model.Deduction
import com.example.order.model.Order
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderStatus
import org.springframework.stereotype.Component

// TODO possible enhancement: implement with MapStruct
@Component
class Mapper {
    fun mapToAggregate(placeOrder: PlaceOrderDTO) = with(placeOrder) {
        CreateOrder(
            createdAt = createdAt,
            paymentBankAccountNumber = paymentBankAccountNumber,
            totalAmount = totalAmount,
            status = OrderStatus.NEW,
            deductions = deductions.map { Deduction(sku = it.sku, quantity = it.quantity) })
    }

    fun mapToDto(order: Order) = with(order) {
        OrderPlaced(
            number = number,
            createdAt = createdAt
        )
    }
}