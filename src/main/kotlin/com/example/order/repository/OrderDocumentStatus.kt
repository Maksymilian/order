package com.example.order.repository

enum class OrderDocumentStatus {
    NEW,
    INSUFFICIENT_STOCK,
    INVENTORY_SERVICE_NOT_AVAILABLE,
    PLACED,
    PREPARED,
    SENT,
    RECEIVED,
}
