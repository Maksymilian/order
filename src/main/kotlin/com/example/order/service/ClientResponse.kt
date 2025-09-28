package com.example.order.service

sealed interface ClientResponse

data class ClientError(val error: String) : ClientResponse

data class ClientSuccess(val message: String?) : ClientResponse
