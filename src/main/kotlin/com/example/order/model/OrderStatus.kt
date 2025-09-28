package com.example.order.model

enum class OrderStatus {
    NEW,
    INSUFFICIENT_STOCK,
    INVENTORY_SERVICE_NOT_AVAILABLE,
    PLACED,
    PREPARED,
    SENT,
    RECEIVED,
}
