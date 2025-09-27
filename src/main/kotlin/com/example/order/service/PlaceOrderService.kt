package com.example.order.service

import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.model.Order
import com.example.order.repository.OrdersRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Mono

@Service
class PlaceOrderService(
    val mapper: Mapper,
    val ordersRepository: OrdersRepository,
    val transactionalOperator: TransactionalOperator
) {

    fun placeOrder(product: CreateOrder): Mono<Order> {
        TODO("Not yet implemented")
//        TODO napisać generowanie numeru zamówienia, utworzenie zamówienia, wysłanie rezerwacji do inventory i zmianę statusu (równolegle),
//        TODO wycofać jeśli jest problem
//        TODO do dodania jest serwis który przekaże dane do inventory poprzez WebClient
//        TODO w testach placeOrder() użyć StepVerifier, w testach wysyłki danych do order użyć Pact Mock Server
//        TODO do developmentu użyć Pact Stub Server
//        TODO spróbować użyć Cline
//        TODO podłączyć filtr oAuth2.0 client do WebClient
//        TODO spróbować użyć Cline i Flyway do wygenerowania danych testowych i kolekcji z indeksami
//        TODO usunięcie wszystkiego z db jeśli wystąpi błąd w REST
//        TODO usunięcie wszystkiego poprzez REST jeśli wystąpił błąd w mongo
    }
}