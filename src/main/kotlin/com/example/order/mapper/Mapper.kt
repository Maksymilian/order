package com.example.order.mapper

import com.example.order.dto.OrderNotPlacedDTO
import com.example.order.dto.OrderPlacedDTO
import com.example.order.dto.PlaceOrderDTO
import com.example.order.model.CreateOrder
import com.example.order.model.OrderNotPlaced
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderResult
import com.example.order.model.OrderStatus
import com.example.order.repository.OrderDocument
import org.springframework.stereotype.Component
import com.example.order.model.Deduction as DeductionEntity
import com.example.order.repository.Deduction as DeductionDocument

// TODO possible enhancement: implement with MapStruct
@Component
class Mapper {
    fun mapToCommand(placeOrder: PlaceOrderDTO) =
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
            is OrderPlaced -> OrderPlacedDTO(id = order.id, createdAt = order.createdAt, number = order.number)
            is OrderNotPlaced -> OrderNotPlacedDTO(reason = order.reason)
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
}
